package com.everhomes.statistics.transaction;

import java.math.BigDecimal;

public class BizRefundOrder {

	private String refundOrderNo;
	
	private Byte refundStatus;
	
	private String shopNo;
	
	private Long refundTime;
	
	private BigDecimal refundAmount;
	
	private String buyerUserId;
	
	private Byte payType;

	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}

	public Byte getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Byte refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public Long getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Long refundTime) {
		this.refundTime = refundTime;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	
}
