package com.everhomes.archives;

import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArchivesNotificationAction implements Runnable {

    @Autowired
    ScheduleProvider scheduleProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesServiceImpl.class);

    private Integer hour;

    @Override
    public void run() {
        LOGGER.warn("ArchivesNotification has been executed!");
    }
}