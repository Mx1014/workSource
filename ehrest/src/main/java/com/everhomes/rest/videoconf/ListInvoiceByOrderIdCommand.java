package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * orderId: 订单号
 *
 */
public class ListInvoiceByOrderIdCommand {
	
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
