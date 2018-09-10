package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司Id，必填</li>
 * <li>meetingReservationId: 会议预订ID，有值时编辑会议时间（也会锁定时间），无值时锁定会议时间</li>
 * <li>meetingRoomId: 会议室ID，必填</li>
 * <li>meetingDate: 会议日期时间戳，不包含时分秒,必填</li>
 * <li>beginTime: 会议预定开始时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000，必填</li>
 * <li>endTime: 会议预定结束的时间戳(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000，必填</li>
 * </ul>
 */
public class MeetingReservationLockTimeCommand {
    private Long organizationId;
    private Long meetingReservationId;
    private Long meetingRoomId;
    private Long meetingDate;
    private Long beginTime;
    private Long endTime;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getMeetingReservationId() {
        return meetingReservationId;
    }

    public void setMeetingReservationId(Long meetingReservationId) {
        this.meetingReservationId = meetingReservationId;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Long meetingDate) {
        this.meetingDate = meetingDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
