package com.everhomes.rest.techpark.park;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * 	<li>payStatus : 支付状态:success,fail</li>
 *	<li>orderNo : 订单号</li>
 *</ul>
 *
 */
public class PaymentRankingCommand {

	@NotNull
	private String payStatus;
	@NotNull
	private String orderNo;
	
	public String getPayStatus() {
		return payStatus;
	}
	
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
