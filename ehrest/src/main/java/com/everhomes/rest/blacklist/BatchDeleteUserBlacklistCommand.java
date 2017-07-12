package com.everhomes.rest.blacklist;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerType: 范围类型 EhOrganizations，EhCommunities..</li>
 * <li>ownerId: 范围id</li>
 * <li>blacklistIds: 黑名单集</li>
 * </ul>
 */
public class BatchDeleteUserBlacklistCommand {

    private String ownerType;

    private Long ownerId;

    @ItemType(Long.class)
    private List<Long> blacklistIds;

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

    public List<Long> getBlacklistIds() {
        return blacklistIds;
    }

    public void setBlacklistIds(List<Long> blacklistIds) {
        this.blacklistIds = blacklistIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
