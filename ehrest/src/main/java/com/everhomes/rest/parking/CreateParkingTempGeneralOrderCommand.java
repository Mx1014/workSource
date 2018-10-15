package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderId : 订单id</li>
 * <li>organizationId : 用户公司id</li>
 * </ul>
 */
public class CreateParkingTempGeneralOrderCommand extends CreateParkingTempOrderCommand{
	
	private Long orderId;
	private Long organizationId;
	
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

}
