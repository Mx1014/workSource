package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId : 订单id</li>
 * </ul>
 */
public class CreateCubicleOrderBackgroundResponse {
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
