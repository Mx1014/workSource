package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>meetingRecordId: 会议纪要id</li>
 * <li>organizationId: 总公司ID</li>
 * </ul>
 */
public class MeetingRecordDetailActionData {
    private Long meetingRecordId;
    private Long organizationId;

    public MeetingRecordDetailActionData() {

    }

    public MeetingRecordDetailActionData(Long meetingRecordId, Long organizationId) {
        this.meetingRecordId = meetingRecordId;
        this.organizationId = organizationId;
    }

    public Long getMeetingRecordId() {
        return meetingRecordId;
    }

    public void setMeetingRecordId(Long meetingRecordId) {
        this.meetingRecordId = meetingRecordId;
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
