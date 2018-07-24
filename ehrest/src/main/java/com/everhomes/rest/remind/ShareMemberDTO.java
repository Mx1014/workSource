package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sourceId: 人员档案ID，必填</li>
 * <li>sourceType: 共享人类型，目前只分享给个人，参考{@link com.everhomes.rest.remind.ShareMemberSourceType}，必填</li>
 * <li>sourceName: 共享人姓名，必填</li>
 * <li>contactAvatar:头像</li>
 * </ul>
 */
public class ShareMemberDTO {
    private Long sourceId;
    private String sourceType;
    private String sourceName;
    private String contactAvatar;

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
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
