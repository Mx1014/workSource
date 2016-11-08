package com.bosigao2.rest;

import com.everhomes.util.StringHelper;

public class Bosigao2CardInfo {
	private String cardCode;
	private String userName;
	private String mobile;
	private String plateNo;
	private String currentPos;
	private String expireDate;
	private String monthlyFee;
	private String cardDescript;
	private String address;
	private String cardState;
	private String chargeType;
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getCurrentPos() {
		return currentPos;
	}
	public void setCurrentPos(String currentPos) {
		this.currentPos = currentPos;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getMonthlyFee() {
		return monthlyFee;
	}
	public void setMonthlyFee(String monthlyFee) {
		this.monthlyFee = monthlyFee;
	}
	public String getCardDescript() {
		return cardDescript;
	}
	public void setCardDescript(String cardDescript) {
		this.cardDescript = cardDescript;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCardState() {
		return cardState;
	}
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
