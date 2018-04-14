package com.everhomes.archives;

import com.everhomes.flow.FlowServiceImpl;
import com.everhomes.scheduler.ScheduleProvider;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

@Component
public class ArchivesNotificationJob extends QuartzJobBean {

    @Autowired
    ArchivesService archivesService;

    @Autowired
    ScheduleProvider scheduleProvider;

    private static final String ARCHIVES_NOTIFICATION = "archives_notification";

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowServiceImpl.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
//            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime nowDateTime = LocalDateTime.now();
            archivesService.executeArchivesNotification(nowDateTime.getDayOfWeek().getValue(), nowDateTime.getHour(), nowDateTime);
            LOGGER.info("======================================== ArchivesNotificationJob has been executed at " + nowDateTime);
/*
            LocalDateTime nextDateTime = nowDateTime.plusHours(1);
            nextDateTime = LocalDateTime.of(nextDateTime.getYear(), nextDateTime.getMonthValue(), nextDateTime.getDayOfMonth(), nextDateTime.getHour(), 0);
            ZonedDateTime zdt = nextDateTime.atZone(zoneId);
            java.util.Date date = java.util.Date.from(zdt.toInstant());
            scheduleProvider.scheduleSimpleJob(
                    ARCHIVES_NOTIFICATION + date,
                    ARCHIVES_NOTIFICATION + date,
                    date,
                    ArchivesNotificationJob.class,
                    new HashMap<>());
            LOGGER.info("-------------------------------------- Next ArchivesNotificationJob has been prepared at" + date);*/
        } catch (Exception e) {
            LOGGER.error("======================================== ArchivesNotificationJob Failed!", e);
        }
    }
}
