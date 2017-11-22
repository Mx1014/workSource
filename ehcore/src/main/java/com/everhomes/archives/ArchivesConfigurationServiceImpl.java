package com.everhomes.archives;


import com.everhomes.queue.taskqueue.JesqueClientFactory;
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
    JesqueClientFactory jesqueClientFactory;

    @Autowired
    ScheduleProvider scheduleProvider;

    private String ARCHIVES_NOTIFICATION = "archivesNotification";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String triggerName = "ArchivesConfigurationTrigger-" + System.currentTimeMillis();
        String jobName = "ArchiveConfigurationName-" + System.currentTimeMillis();
        String cronExpression = "0 0 4 * * ?";
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, ArchivesConfigurationtJob.class, new HashMap());
    }


    @Override
    public void sendingMail(Integer hour, List<ArchivesNotifications> notifyLists) {
        final Job job = new Job(ArchivesNotificationAction.class.getName(), new Object[]{notifyLists});
        jesqueClientFactory.getClientPool().delayedEnqueue(ARCHIVES_NOTIFICATION, job, System.currentTimeMillis() + hour * 60 * 60 * 1000);
    }
}
