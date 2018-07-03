package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>meetingRoomId: 会议室ID</li>
 * </ul>
 */
public class GetMeetingRoomSimpleInfoCommand {
    private Long organizationId;
    private Long meetingRoomId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
