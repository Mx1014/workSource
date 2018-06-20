package com.everhomes.scheduler;

import com.everhomes.meeting.MeetingService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 会议即将开始提醒
 */
@Component
public class MeetingRemindScheduleJob extends QuartzJobBean {
    public static final String MEETING_REMIND_SCHEDULE = "meeting-remind-";
    public static String CRON_EXPRESSION = "0 0/2 * * * ?";

    @Autowired
    private MeetingService meetingService;
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            meetingService.meetingRemind();
        }
    }
}
