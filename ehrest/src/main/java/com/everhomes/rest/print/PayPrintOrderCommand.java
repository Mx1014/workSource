// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>orderId : 订单id</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PayPrintOrderCommand {
	private Long orderId;

	public PayPrintOrderCommand() {
	}

	public PayPrintOrderCommand(Long orderId) {
		this.orderId = orderId;
	}

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
