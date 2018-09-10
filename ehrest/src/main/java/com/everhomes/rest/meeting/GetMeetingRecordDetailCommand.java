package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>meetingRecordId: 会议纪要ID</li>
 * </ul>
 */
public class GetMeetingRecordDetailCommand {
    private Long organizationId;
    private Long meetingRecordId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getMeetingRecordId() {
        return meetingRecordId;
    }

    public void setMeetingRecordId(Long meetingRecordId) {
        this.meetingRecordId = meetingRecordId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
