package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>rentalBillId: 订单id</li>
 * <li>rentalType: rentalType</li>
 * <li>timeStep: timeStep</li>
 * <li>endTime: 续费结束时间</li>
 * <li>amount: 金额</li>
 * </ul>
 */
public class RenewRentalOrderCommand {
    private Long rentalBillId;
    private Byte rentalType;
    private Double timeStep;

    private Double cellCount;

    private Long endTime;

    private BigDecimal amount;

    private String clientAppName;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public Double getCellCount() {
        return cellCount;
    }

    public void setCellCount(Double cellCount) {
        this.cellCount = cellCount;
    }

    public Double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(Double timeStep) {
        this.timeStep = timeStep;
    }

    public Long getRentalBillId() {
        return rentalBillId;
    }

    public void setRentalBillId(Long rentalBillId) {
        this.rentalBillId = rentalBillId;
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
