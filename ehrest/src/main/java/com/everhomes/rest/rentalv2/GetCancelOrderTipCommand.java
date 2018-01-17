package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2018/1/17.
 */
public class GetCancelOrderTipCommand {

    private Long orderId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
