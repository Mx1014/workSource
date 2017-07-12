package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>meterId: 表记id</li>
 *     <li>settingType: 设置类型{@link com.everhomes.rest.energy.EnergyMeterSettingType}</li>
 * </ul>
 */
public class ListEnergyMeterSettingLogsCommand {

    @NotNull private Long organizationId;
    @NotNull private Long meterId;
    @NotNull private Byte settingType;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getSettingType() {
        return settingType;
    }

    public void setSettingType(Byte settingType) {
        this.settingType = settingType;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
