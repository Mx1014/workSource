package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>ownerId: 所属组织id</li>
 *     <li>ownerType: 所属组织类型</li>
 *     <li>communityId: 所属园区id</li>
 *     <li>meterType: 表记类型 {@link com.everhomes.rest.energy.EnergyMeterType}</li>
 * </ul>
 */
public class ListEnergyDefaultSettingsCommand {

    @NotNull private Long ownerId;
    @NotNull private String ownerType;
    @NotNull private Long communityId;
    private Byte meterType;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
