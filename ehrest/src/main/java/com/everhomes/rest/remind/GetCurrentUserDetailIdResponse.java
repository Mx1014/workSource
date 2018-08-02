package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 通讯录关联的用户ID</li>
 * <li>detailId: 联系人档案id</li>
 * <li>contactName: 联系人名字</li>
 * <li>contactToken: 联系人号码</li>
 * </ul>
 */
public class GetCurrentUserDetailIdResponse {
    private Long userId;
    private Long detailId;
    private String contactName;
    private String contactToken;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
