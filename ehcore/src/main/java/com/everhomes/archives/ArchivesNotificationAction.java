package com.everhomes.archives;

import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.archives.ArchivesUtil;
import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArchivesNotificationAction implements Runnable {

    @Autowired
    ScheduleProvider scheduleProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesServiceImpl.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private List<ArchivesNotifications> notifyLists;

//    private Integer hour;

    @Override
    public void run() {

        //  1.current time and one month later
        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        Date startDate = new Date(date.getTime());
        Date endDate = new Date(calendar.getTime().getTime());
//        date = calendar.getTime();

        //  2.birthday
//        List<OrganizationMemberDetails>
        //  3.
        LOGGER.warn("ArchivesNotification has been executed!");
    }
}