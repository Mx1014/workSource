package com.everhomes.statistics.transaction;

import java.math.BigDecimal;

public class PaidTransaction {

	private String payNo;
	
	private String orderNo;
	
	private String orderType;
	
	private String onlinePayStyleNo;
	
	private BigDecimal paidAmount;
	
	private Long paidTime;
	
	private Byte payType;

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOnlinePayStyleNo() {
		return onlinePayStyleNo;
	}

	public void setOnlinePayStyleNo(String onlinePayStyleNo) {
		this.onlinePayStyleNo = onlinePayStyleNo;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Long getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(Long paidTime) {
		this.paidTime = paidTime;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	
	
}
