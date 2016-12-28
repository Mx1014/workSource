package com.everhomes.rest.energy;

import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>settingId: 设置id</li>
 *     <li>settingValue: 属性值</li>
 *     <li>formulaId: 如果修改的是公式,则为公式id</li>
 *     <li>settingStatus: 状态 {@link com.everhomes.rest.energy.EnergyCommonStatus}</li>
 * </ul>
 */
public class UpdateEnergyMeterDefaultSettingCommand {

    @NotNull private Long organizationId;
    @NotNull private Long settingId;
    private BigDecimal settingValue;
    @EnumType(value = EnergyCommonStatus.class, nullValue = true)
    private Byte settingStatus;
    private Long formulaId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public BigDecimal getSettingValue() {
        return settingValue;
    }

    public Byte getSettingStatus() {
        return settingStatus;
    }

    public void setSettingStatus(Byte settingStatus) {
        this.settingStatus = settingStatus;
    }

    public void setSettingValue(BigDecimal settingValue) {
        this.settingValue = settingValue;
    }

    public Long getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Long formulaId) {
        this.formulaId = formulaId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
