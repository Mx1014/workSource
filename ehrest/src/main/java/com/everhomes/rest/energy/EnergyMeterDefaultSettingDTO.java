package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 属性名称</li>
 *     <li>settingValue: 属性值</li>
 *     <li>formulaName: 公式名称</li>
 *     <li>formulaType: 公式类型 {@link com.everhomes.rest.energy.EnergyFormulaType}</li>
 *     <li>settingStatus: 状态 {@link com.everhomes.rest.energy.EnergyCommonStatus}</li>
 * </ul>
 */
public class EnergyMeterDefaultSettingDTO {

    private Long id;
    private String name;
    private BigDecimal settingValue;
    private String formulaName;
    private String meterType;
    private Byte formulaType;
    private Byte settingStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
