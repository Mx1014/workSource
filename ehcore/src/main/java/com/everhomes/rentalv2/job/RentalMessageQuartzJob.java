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
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/11/17.
 */
@Component
public class RentalMessageQuartzJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalMessageQuartzJob.class);

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            Long orderId = jobMap.getLong("orderId");
            String methodName = jobMap.getString("methodName");
            String beanName = jobMap.getString("beanName");

            RentalOrder bill = rentalv2Provider.findRentalBillById(orderId);

            //短信
            RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(bill.getResourceType());

            Class clz = handler.getClass();
            Method method = clz.getMethod(methodName, RentalOrder.class);

            if (null != method) {
                method.invoke(handler, bill);
            }

        }catch (Exception e) {
            LOGGER.error("RentalMessageJob error", e);
        }

    }

}
