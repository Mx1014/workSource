package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>meterType: 表记类型 {@link com.everhomes.rest.energy.EnergyMeterType}</li>
 * </ul>
 */
public class ListEnergyDefaultSettingsCommand {

    @NotNull private Long organizationId;
    @NotNull private Byte meterType;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
