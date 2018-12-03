package com.everhomes.rentalv2.job;

import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalMessageHandler;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.Rentalv2Provider;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RentalNearEndMessageJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalNearEndMessageJob.class);
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private Rentalv2Provider rentalv2Provider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("start RentalNearEndMessageJob ");
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            String orderId = jobMap.getString("orderId");
            String resourceType = jobMap.getString("resourceType");
            RentalMessageHandler messageHandler = rentalCommonService.getRentalMessageHandler(resourceType);
            RentalOrder order = rentalv2Provider.findRentalBillById(Long.valueOf(orderId));
            messageHandler.orderNearEndSendMessage(order);

        }catch (Exception e) {
            LOGGER.error("RentalMessageJob error", e);
        }
    }
}
