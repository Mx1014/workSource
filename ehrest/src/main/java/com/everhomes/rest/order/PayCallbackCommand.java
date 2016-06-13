package com.everhomes.rest.order;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *	<li>orderNo : 订单号</li>
 * 	<li>payStatus : 支付状态:success,fail</li>
 *	<li>orderType : 订单类型,详见{@link com.everhomes.rest.order.OrderType}</li>
 * 	<li>vendorType : 支付方式,10001-支付宝，10002-微信</li>
 *	<li>payTime : 支付日期</li>
 *	<li>payAmount : 支付金额</li>
 *	<li>payAccount : 支付账号</li>
 * 	<li>payObj : 第三方支付通知返回的所有参数key-value字符串</li>
 *</ul>
 *
 */
public class PayCallbackCommand {
	
	@NotNull
	private String orderNo;
	@NotNull
	private String payStatus;
	@NotNull
	private String orderType;
	@NotNull
	private String vendorType;
	private String payTime;
	private String payAmount;
	private String payAccount;
	private String payObj;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
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
	public String getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	public String getPayObj() {
		return payObj;
	}
	public void setPayObj(String payObj) {
		this.payObj = payObj;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
