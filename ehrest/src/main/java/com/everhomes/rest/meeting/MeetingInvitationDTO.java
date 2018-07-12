package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sourceType: 目前只支持个人，必填，参考{@link com.everhomes.rest.meeting.MeetingMemberSourceType}</li>
 * <li>sourceId: 员工detailId，必填</li>
 * <li>sourceName: 员工姓名，必填</li>
 * <li>contactAvatar:头像,后台返回的，前端不用传</li>
 * </ul>
 */
public class MeetingInvitationDTO {
    private String sourceType;
    private Long sourceId;
    private String sourceName;
    private String contactAvatar;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
