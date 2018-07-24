package com.everhomes.scheduler;

import com.everhomes.remind.RemindService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 日程定时提醒任务
 */
@Component
public class CalendarRemindScheduleJob extends QuartzJobBean {
    public static final String CALENDAR_REMIND_SCHEDULE = "calendar-remind-";
    public static String CRON_EXPRESSION = "0 0 9 * * ?";

    @Autowired
    private RemindService remindService;
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 现网是集群部署，这个判断是为了防止定时任务在多台机器执行
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            remindService.remindSchedule();
        }
    }
}
