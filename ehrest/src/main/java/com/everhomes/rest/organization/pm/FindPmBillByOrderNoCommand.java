package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>orderNo : 订单号</li>
 *</ul>
 *	
 */
public class FindPmBillByOrderNoCommand {
	@NotNull
	private String orderNo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
}
