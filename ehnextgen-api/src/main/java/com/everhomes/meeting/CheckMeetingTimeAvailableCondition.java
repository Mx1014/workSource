package com.everhomes.meeting;

import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;

public class CheckMeetingTimeAvailableCondition {
    private Long meetingRoomId;
    private Date meetingDate;
    private Timestamp beginDateTime;
    private Timestamp endDateTime;
    private Long meetingReservationId;

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Timestamp getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Timestamp beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getMeetingReservationId() {
        return meetingReservationId;
    }

    public void setMeetingReservationId(Long meetingReservationId) {
        this.meetingReservationId = meetingReservationId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
