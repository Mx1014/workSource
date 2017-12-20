package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>id: 订单id</li>
 * <li>delayEndTime: 延长结束时间</li>
 * <li>reserveCellCount: 单元格数量</li>
 * <li>delayAmount: 金额</li>
 * </ul>
 */
public class DelayReserveOrderCommand {
    private Long id;

    private Long delayEndTime;
    private Double reserveCellCount;

    private BigDecimal delayAmount;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getDelayEndTime() {
        return delayEndTime;
    }

    public void setDelayEndTime(Long delayEndTime) {
        this.delayEndTime = delayEndTime;
    }

    public Double getReserveCellCount() {
        return reserveCellCount;
    }

    public void setReserveCellCount(Double reserveCellCount) {
        this.reserveCellCount = reserveCellCount;
    }

    public BigDecimal getDelayAmount() {
        return delayAmount;
    }

    public void setDelayAmount(BigDecimal delayAmount) {
        this.delayAmount = delayAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
