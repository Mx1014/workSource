package com.everhomes.workReport;

import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class WorkReportRxMsgServiceImpl implements WorkReportAutoMsgService,ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkReportRxMsgServiceImpl.class);


    @Autowired
    JesqueClientFactory jesqueClientFactory;

    @Autowired
    ScheduleProvider scheduleProvider;

    private static final String WORK_REPORT_RX_MSG = "work_report_rx_msg";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
/*
        String triggerName = WORK_REPORT_RX_MSG + System.currentTimeMillis();
        String jobName = WORK_REPORT_RX_MSG + System.currentTimeMillis();
        String cronExpression = "0 0 0/1 * * ?";
        LOGGER.info("The first ArchivesNotificationJob has been prepared at " + LocalDateTime.now());
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, ArchivesNotificationJob.class, new HashMap());
*/

    }
}
