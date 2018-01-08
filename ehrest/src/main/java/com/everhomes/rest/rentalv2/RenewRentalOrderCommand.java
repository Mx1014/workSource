package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * @author sw on 2018/1/8.
 */
public class RenewRentalOrderCommand {
    private Long rentalBillId;
    private String resourceType;

	private Long endTime;

	private BigDecimal amount;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getRentalBillId() {
        return rentalBillId;
    }

    public void setRentalBillId(Long rentalBillId) {
        this.rentalBillId = rentalBillId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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
