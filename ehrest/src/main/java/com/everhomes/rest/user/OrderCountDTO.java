package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class OrderCountDTO {

	private String orderCount;

	public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
