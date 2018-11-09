package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

public class PrintOrderActionData implements Serializable{

	private static final long serialVersionUID = -7134989050392679006L;
	private Long orderId;
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
