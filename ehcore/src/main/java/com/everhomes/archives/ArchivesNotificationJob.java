package com.everhomes.archives;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.flow.FlowServiceImpl;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ArchivesNotificationJob extends QuartzJobBean {

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    private static final String ARCHIVES_NOTIFICATION = "archives_notification";

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowServiceImpl.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                LocalDateTime nowDateTime = LocalDateTime.now();
                coordinationProvider.getNamedLock(CoordinationLocks.ARCHIVES_NOTIFICATION.getCode()).tryEnter(() -> {
                    LOGGER.info("ArchivesNotificationJob has been started at " + nowDateTime);
                    archivesService.executeArchivesNotification(nowDateTime.getDayOfWeek().getValue(), nowDateTime.getHour(), nowDateTime);
                    LOGGER.info("ArchivesNotificationJob has been ended at " + nowDateTime);
                });
            }
        } catch (Exception e) {
            LOGGER.error("ArchivesNotificationJob Failed!", e);
        }
    }
}
