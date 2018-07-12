package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>meetingReservationId: 会议预订id</li>
 * <li>organizationId: 总公司ID</li>
 * </ul>
 */
public class MeetingReservationDetailActionData {
    private Long meetingReservationId;
    private Long organizationId;

    public MeetingReservationDetailActionData() {

    }

    public MeetingReservationDetailActionData(Long meetingReservationId, Long organizationId) {
        this.meetingReservationId = meetingReservationId;
        this.organizationId = organizationId;
    }

    public Long getMeetingReservationId() {
        return meetingReservationId;
    }

    public void setMeetingReservationId(Long meetingReservationId) {
        this.meetingReservationId = meetingReservationId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
