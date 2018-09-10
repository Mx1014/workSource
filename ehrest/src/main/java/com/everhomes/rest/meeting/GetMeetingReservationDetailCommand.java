package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>meetingReservationId: 会议ID</li>
 * </ul>
 */
public class GetMeetingReservationDetailCommand {
    private Long organizationId;
    private Long meetingReservationId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
