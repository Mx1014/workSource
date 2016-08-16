package com.everhomes.statistics.transaction;

import java.math.BigDecimal;

public class BizPaidOrder {

	private String orderNo;
	
	private Byte status;
	
	private String shopNo;
	
	private Long paidTime;
	
	private BigDecimal paidAmount;
	
	private String buyerUserId;
	
	private Byte payType;
	
	private Byte shopCreateType;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public Long getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(Long paidTime) {
		this.paidTime = paidTime;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
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

	public Byte getShopCreateType() {
		return shopCreateType;
	}

	public void setShopCreateType(Byte shopCreateType) {
		this.shopCreateType = shopCreateType;
	}
	
	
}
