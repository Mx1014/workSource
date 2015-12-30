package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderNo : 订单号</li>
 *	<li>payAmount : 支付金额</li>
 *	<li>billName : 物业账单名称</li>
 *</ul>
 *
 */
public class PmBillForOrderNoDTO {

	private String orderNo;
	private BigDecimal payAmount;
	private String billName;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}



}
