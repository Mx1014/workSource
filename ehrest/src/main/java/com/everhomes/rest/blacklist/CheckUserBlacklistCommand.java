package com.everhomes.rest.blacklist;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 范围类型 EhOrganizations，EhCommunities..</li>
 * <li>ownerId: 范围id</li>
 * <li>contactToken: 手机号码</li>
 * </ul>
 */
public class CheckUserBlacklistCommand {

    private String ownerType;

    private Long ownerId;

    private String contactToken;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
