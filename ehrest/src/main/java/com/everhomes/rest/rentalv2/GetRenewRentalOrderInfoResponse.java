package com.everhomes.rest.rentalv2;

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
public class GetRenewRentalOrderInfoResponse {

    private Long rentalBillId;
    private Byte rentalType;
    private Double timeStep;

    private Long endTime;

    private BigDecimal amount;

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
