package com.everhomes.customer;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class TrackingPlanNotifytJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingPlanNotifytJob.class);

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();

            Long recordId = (Long)jobMap.get("trackingPlanId");

            if (enterpriseCustomerProvider.updateTrackingPlanNotify(recordId)) {
            	CustomerTrackingPlan plan = enterpriseCustomerProvider.findCustomerTrackingPlanById(recordId);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("TrackingPlanNotifytJob.run success record = {}", plan);
                }
                //update ok, means we take it's owner
                customerService.processTrackingPlanNotify(plan);
            } else {
                LOGGER.warn("TrackingPlanNotifytJob.run failure recordId = {}", recordId);
            }
        } catch (Exception e) {
            LOGGER.error("TrackingPlanNotifytJob error", e);
        }
    }
}
