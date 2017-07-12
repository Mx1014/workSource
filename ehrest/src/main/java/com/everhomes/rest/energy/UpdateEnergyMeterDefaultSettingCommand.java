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
 *     <li>calculationType: 价格计算方式 参考{@link com.everhomes.rest.energy.PriceCalculationType} </li>
 *     <li>configId: 价格方案id </li>
 * </ul>
 */
public class UpdateEnergyMeterDefaultSettingCommand {

    @NotNull private Long organizationId;
    @NotNull private Long settingId;
    private BigDecimal settingValue;
    @EnumType(value = EnergyCommonStatus.class, nullValue = true)
    private Byte settingStatus;
    private Long formulaId;

    private Byte calculationType;
    private Long configId;
    private Long ownerId;
    private String ownerType;

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

    public Byte getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Byte calculationType) {
        this.calculationType = calculationType;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
