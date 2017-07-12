package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerId: 拥有者id</li>
 *     <li>ownerType: 拥有者类型</li>
 *     <li>communityId: 园区id</li>
 * </ul>
 */
public class ListEnergyMeterPriceConfigCommand {

    private Long communityId;

    private String ownerType;

    private Long ownerId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
