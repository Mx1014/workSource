package com.everhomes.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;



/**
 * <ul>
 * 	<li>payStatus : 支付状态:success,failure</li>
 * 	<li>description : 描述</li>
 *
 *	<li>orderNo : 订单号</li>
 * 	<li>vendorType : 支付方式,10001-支付宝，10002-微信</li>
 *
 *	<li>payTime : 支付日期</li>
 *	<li>payAmount : 支付金额</li>
 *</ul>
 *
 */
public class OnlinePayPmBillCommand {
	
	@NotNull
	private String payStatus;
	private String description;
	
	private String orderNo;
	private String vendorType;
	
	private String payTime;
	private String payAmount;
	//private String feeType;
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	
	
	
	
	
	
}
