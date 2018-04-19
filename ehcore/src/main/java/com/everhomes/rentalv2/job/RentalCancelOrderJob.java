package com.everhomes.rentalv2.job;

import com.everhomes.db.DbProvider;
import com.everhomes.rentalv2.*;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.TransactionStatus;

/**
 * Created by Administrator on 2018/4/19.
 */
public class RentalCancelOrderJob extends QuartzJobBean {
    @Autowired
    private Rentalv2Provider rentalProvider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private DbProvider dbProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
        Long orderId = jobMap.getLong("orderId");
        RentalOrder rentalBill = rentalProvider.findRentalBillById(orderId);
        if(null == rentalBill)
            return ;
        if (rentalBill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode()) ) {

            dbProvider.execute((TransactionStatus status) -> {
                rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
                rentalProvider.updateRentalBill(rentalBill);

                RentalOrderHandler orderHandler = rentalCommonService.getRentalOrderHandler(rentalBill.getResourceType());
                orderHandler.releaseOrderResourceStatus(rentalBill);
                return null;
            });
            //发消息
            RentalMessageHandler messageHandler = rentalCommonService.getRentalMessageHandler(rentalBill.getResourceType());

            messageHandler.sendOrderOverTimeMessage(rentalBill);
        }
    }
}
