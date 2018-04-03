package com.everhomes.statistics.transaction;


import java.math.BigDecimal;

public class BizBusinessInfo {

	private String shopAddress;
	
	private String shopCoordination;

	private String shopLogo;

	private String shopName;

	private String shopNo;

	private String shopSign;

	private Byte status;

	private String shopTypeNo;

	private String shopModel;

	private BigDecimal deliveryFee;

	private String deliveryType;

	private String phone;

	private Integer sellScope;

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getShopCoordination() {
		return shopCoordination;
	}

	public void setShopCoordination(String shopCoordination) {
		this.shopCoordination = shopCoordination;
	}

	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getShopSign() {
		return shopSign;
	}

	public void setShopSign(String shopSign) {
		this.shopSign = shopSign;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getShopTypeNo() {
		return shopTypeNo;
	}

	public void setShopTypeNo(String shopTypeNo) {
		this.shopTypeNo = shopTypeNo;
	}

	public String getShopModel() {
		return shopModel;
	}

	public void setShopModel(String shopModel) {
		this.shopModel = shopModel;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getSellScope() {
		return sellScope;
	}

	public void setSellScope(Integer sellScope) {
		this.sellScope = sellScope;
	}
}
