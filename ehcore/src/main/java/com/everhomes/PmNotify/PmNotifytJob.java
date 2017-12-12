package com.everhomes.PmNotify;

import com.everhomes.pmNotify.PmNotifyProvider;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.pmNotify.PmNotifyService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * Created by ying.xiong on 2017/9/12.
 */
@Component
public class PmNotifytJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmNotifytJob.class);

    @Autowired
    private PmNotifyProvider pmNotifyProvider;

    @Autowired
    private PmNotifyService pmNotifyService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();

            Long recordId = (Long)jobMap.get("pmNotifyRecordId");

            if (pmNotifyProvider.updateIfUnsend(recordId)) {
                PmNotifyRecord record = pmNotifyProvider.findRecordById(recordId);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("PmNotifyRecord.run success record = {}", record);
                }
                //update ok, means we take it's owner
                pmNotifyService.processPmNotifyRecord(record);
            } else {
                LOGGER.warn("PmNotifyRecord.run failure recordId = {}", recordId);
            }
        } catch (Exception e) {
            LOGGER.error("PmNotifytJob error", e);
        }
    }
}
