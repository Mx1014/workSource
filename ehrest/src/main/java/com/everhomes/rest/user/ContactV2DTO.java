package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID</li>
 * <li>contactId:联系人ID</li>
 * <li>contactPhone:联系人手机</li>
 * <li>contactName:联系人名字</li>
 * <li>contactAvatar:头像</li>
 * <li>userId:通讯录关联的用户ID</li>
 * <li>createTime:创建通讯录时间</li>
 * </ul>
 */
public class ContactV2DTO {
    private String entityType;
    private Long entityId;
    private Long contactId;
    private String contactPhone;
    private String contactName;
    private String contactAvatar;
    private Long userId;
    private Long createTime;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
