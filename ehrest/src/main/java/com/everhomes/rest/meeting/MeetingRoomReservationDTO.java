package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>meetingDate: 查询日期时间戳，仅包含日期</li>
 * <li>reservationTimeDTOS: 查询日期对应当日的会议室预约时间情况，参考{@link com.everhomes.rest.meeting.MeetingRoomReservationTimeDTO}</li>
 * <li>bookedUpFlag: 1表示已订满，0表示未订满，参考{@link com.everhomes.rest.meeting.MeetingGeneralFlag}</li>
 * </ul>
 */
public class MeetingRoomReservationDTO {
    private Long meetingDate;
    private List<MeetingRoomReservationTimeDTO> reservationTimeDTOS;
    private Byte bookedUpFlag;

    public Long getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Long meetingDate) {
        this.meetingDate = meetingDate;
    }

    public List<MeetingRoomReservationTimeDTO> getReservationTimeDTOS() {
        return reservationTimeDTOS;
    }

    public void setReservationTimeDTOS(List<MeetingRoomReservationTimeDTO> reservationTimeDTOS) {
        this.reservationTimeDTOS = reservationTimeDTOS;
    }

    public Byte getBookedUpFlag() {
        return bookedUpFlag;
    }

    public void setBookedUpFlag(Byte bookedUpFlag) {
        this.bookedUpFlag = bookedUpFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
