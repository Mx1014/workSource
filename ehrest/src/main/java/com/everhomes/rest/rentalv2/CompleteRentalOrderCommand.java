package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2018/1/15.
 */
public class CompleteRentalOrderCommand {
    private Long rentalBillId;

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
}
