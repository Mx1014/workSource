package com.everhomes.archives;


import com.everhomes.scheduler.ScheduleProvider;
import net.greghaines.jesque.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class ArchivesConfigurationServiceImpl implements ArchivesConfigurationService,ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ScheduleProvider scheduleProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String triggerName = "ArchivesConfigurationTrigger-" + System.currentTimeMillis();
        String jobName = "ArchiveConfigurationName-" + System.currentTimeMillis();
        String cronExpression = "0 0 4 * * ?";
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, ArchivesConfigurationtJob.class, new HashMap());
    }


    @Override
    public void sendingMail(List<ArchivesNotifications> notifyLists) {
        final Job job = new Job(ArchviesNotificationAction.class.getName(), new Object[]{notifyLists});

    }
}
