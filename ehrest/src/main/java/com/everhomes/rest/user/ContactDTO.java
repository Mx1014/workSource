package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>id:id</li>
 *         <li>contactPhone:联系人手机</li>
 *         <li>contactName:联系人名字</li>
 *         <li>contactId:联系人左邻ID</li>
 *         <li>createTime:创建通讯录时间</li>
 *         <li>ehUsername:左邻用户名</li>
 *         <li>contactAvatar:头像</li>
 *         <li>communityName:小区名</li>
 *         </ul>
 */
public class ContactDTO {
    private Long id;
    private String contactPhone;
    private String contactName;
    private Long contactId;
    private Long createTime;
    private String ehUsername;
    private String contactAvatar;
    private String communityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEhUsername() {
        return ehUsername;
    }

    public void setEhUsername(String ehUsername) {
        this.ehUsername = ehUsername;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
