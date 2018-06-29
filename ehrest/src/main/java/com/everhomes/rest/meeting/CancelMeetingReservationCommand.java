package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司Id，必填</li>
 * <li>meetingReservationId: 要取消的会议预约ID，必填</li>
 * </ul>
 */
public class CancelMeetingReservationCommand {
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
