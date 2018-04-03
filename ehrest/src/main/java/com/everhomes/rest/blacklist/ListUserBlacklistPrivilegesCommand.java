package com.everhomes.rest.blacklist;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 范围类型 EhOrganizations，EhCommunities..</li>
 * <li>ownerId: 范围id</li>
 * <li>userId: 用户id</li>
 * </ul>
 */
public class ListUserBlacklistPrivilegesCommand {

    private String ownerType;

    private Long ownerId;

    private Long userId;

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
