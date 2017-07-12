package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>meterId: 表记id</li>
 *     <li>settingValue: 属性值</li>
 *     <li>planName: 方案名称</li>
 *     <li>time: 时间</li>
 *     <li>createTime: 创建时间</li>
 * </ul>
 */
public class EnergyMeterPriceDTO {

    private Long id;
    private Long meterId;
    private BigDecimal settingValue;
    private String planName;
    private Timestamp time;
    private Timestamp createTime;

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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
