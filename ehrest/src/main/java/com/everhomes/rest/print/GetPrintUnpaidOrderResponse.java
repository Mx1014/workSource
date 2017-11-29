// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>existFlag : 是否存在未支付订单,存在(0),不存在(1), 参考 {@link com.everhomes.rest.print.PrintLogonStatusType} </li>
 * <li>orderId : 订单id </li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintUnpaidOrderResponse {
	private Byte existFlag;
	private Long orderId;
	
	public GetPrintUnpaidOrderResponse(Byte existFlag) {
		this.existFlag = existFlag;
	}

	public GetPrintUnpaidOrderResponse() {
	}

	public Byte getExistFlag() {
		return existFlag;
	}

	public void setExistFlag(Byte existFlag) {
		this.existFlag = existFlag;
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
