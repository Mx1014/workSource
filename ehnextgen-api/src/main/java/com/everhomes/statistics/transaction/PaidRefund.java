package com.everhomes.statistics.transaction;

import java.math.BigDecimal;

public class PaidRefund {

	private String payNo;
	
	private String orderNo;
	
	private String orderType;
	
	private String onlinePayStyleNo;
	
	private String refundStatus;
	
	private BigDecimal refundAmount;
	
	private Long refundTime;
	
	private Byte payType;
	
	private String refundRecordNo;
	
	private String refundOrderNo;
	

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

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Long getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Long refundTime) {
		this.refundTime = refundTime;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	public String getRefundRecordNo() {
		return refundRecordNo;
	}

	public void setRefundRecordNo(String refundRecordNo) {
		this.refundRecordNo = refundRecordNo;
	}

	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}
	
	
}
