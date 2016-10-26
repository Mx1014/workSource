package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 属性名称</li>
 *     <li>settingValue: 属性值</li>
 *     <li>formulaType: 公式类型 {@link com.everhomes.rest.energy.EnergyFormulaType}</li>
 * </ul>
 */
public class EnergyMeterDefaultSettingDTO{

    private Long id;
    private String name;
    private String settingValue;
    private String meterType;
    private Byte formulaType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
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
