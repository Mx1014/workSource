package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId: 订单Id</li>
 * </ul>
 */
public class GetInvoiceUrlCommand {
	@NotNull
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
