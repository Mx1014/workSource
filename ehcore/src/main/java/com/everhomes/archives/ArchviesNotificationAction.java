package com.everhomes.archives;

import com.everhomes.scheduler.ScheduleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArchviesNotificationAction implements Runnable {

    @Autowired
    ScheduleProvider scheduleProvider;


    private Integer hour;

    @Override
    public void run() {
        System.out.println("123");
    }
}