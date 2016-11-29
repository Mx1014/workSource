package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>maxReading: 最大量程</li>
 *     <li>operateTime: 换表时间</li>
 * </ul>
 */
public class EnergyMeterChangeLogDTO {

    private Long id;
    private BigDecimal maxReading;
    private Timestamp operateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMaxReading() {
        return maxReading;
    }

    public void setMaxReading(BigDecimal maxReading) {
        this.maxReading = maxReading;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
