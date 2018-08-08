package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>meetingReservationId: 预约的会议ID</li>
 * <li>meetingDate: 会议日期，仅包含日期</li>
 * <li>beginTime: 会议开始时间(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * <li>endTime: 会议结束时间(毫秒数)，仅包含时分秒，如8:30等于8*3600*1000+30*60*1000</li>
 * </ul>
 */
public class MeetingRoomReservationTimeDTO {
    private Long meetingReservationId;
    private Long meetingDate;
    private Long beginTime;
    private Long endTime;

    public MeetingRoomReservationTimeDTO() {

    }

    public MeetingRoomReservationTimeDTO(Long meetingReservationId, Long meetingDate, Long beginTime, Long endTime) {
        this.meetingReservationId = meetingReservationId;
        this.meetingDate = meetingDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public Long getMeetingReservationId() {
        return meetingReservationId;
    }

    public void setMeetingReservationId(Long meetingReservationId) {
        this.meetingReservationId = meetingReservationId;
    }

    public Long getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Long meetingDate) {
        this.meetingDate = meetingDate;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
