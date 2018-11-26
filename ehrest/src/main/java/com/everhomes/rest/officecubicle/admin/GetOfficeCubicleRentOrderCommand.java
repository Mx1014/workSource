package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
 * <li>orderId</li>
 * </ul>
 */
public class GetOfficeCubicleRentOrderCommand {

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
