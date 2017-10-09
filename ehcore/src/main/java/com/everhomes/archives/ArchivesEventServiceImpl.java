package com.everhomes.archives;


import com.everhomes.scheduler.ScheduleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ArchivesEventServiceImpl implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ScheduleProvider scheduleProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String triggerName = "ArchivesEventJobTrigger-" + System.currentTimeMillis();
        String jobName = "ArchiveEventJobName-" + System.currentTimeMillis();
        String cronExpression = "0 0 4 * * ?";
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, ArchivesEventJob.class, new HashMap());
    }
}
