package com.everhomes.scheduler;

import com.everhomes.meeting.MeetingService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 会议室预订时间锁定超过10分钟没有完成预订操作将自动回收
 */
@Component
public class MeetingRoomRecoveryScheduleJob extends QuartzJobBean {
    public static final String MEETING_ROOM_RECOVERY_SCHEDULE = "meeting-room-recovery-";
    public static String CRON_EXPRESSION = "0 0/2 * * * ?";

    @Autowired
    private MeetingService meetingService;
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            meetingService.recoveryMeetingRoomResource();
        }
    }
}
