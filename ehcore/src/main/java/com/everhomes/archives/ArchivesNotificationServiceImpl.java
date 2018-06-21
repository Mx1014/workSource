package com.everhomes.archives;


import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;

@Component
public class ArchivesNotificationServiceImpl implements ArchivesNotificationService,ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesNotificationServiceImpl.class);


    @Autowired
    JesqueClientFactory jesqueClientFactory;

    @Autowired
    ScheduleProvider scheduleProvider;

    private static final String ARCHIVES_NOTIFICATION = "archives_notification";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String triggerName = ARCHIVES_NOTIFICATION + System.currentTimeMillis();
        String jobName = ARCHIVES_NOTIFICATION + System.currentTimeMillis();
        String cronExpression = "0 0 0/1 * * ?";
        LOGGER.info("The first ArchivesNotificationJob has been prepared at " + LocalDateTime.now());
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, ArchivesNotificationJob.class, new HashMap());
    }
}
