package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>meterId: 表记id</li>
 *     <li>settingValue: 属性值</li>
 *     <li>formulaName: 公式名称</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>calculationType: 价格计算方式 参考{@link com.everhomes.rest.energy.PriceCalculationType}</li>
 *     <li>priceConfigName: 价格方案名称</li>
 * </ul>
 */
public class EnergyMeterSettingLogDTO {

    private Long id;
    private Long meterId;
    private BigDecimal settingValue;
    private String formulaName;
    private Timestamp startTime;
    private Timestamp endTime;

    private Byte calculationType;
    private String priceConfigName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public BigDecimal getSettingValue() {
        return settingValue;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Byte getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Byte calculationType) {
        this.calculationType = calculationType;
    }

    public String getPriceConfigName() {
        return priceConfigName;
    }

    public void setPriceConfigName(String priceConfigName) {
        this.priceConfigName = priceConfigName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
