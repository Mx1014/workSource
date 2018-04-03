package com.everhomes.parking.wanke;

import com.everhomes.util.StringHelper;

public class WankeCardInfo {
	
	private String userName;
	private String mobile;
	private String plateNo;
	private Boolean valid;
	private String expireDate;
	private String monthlyFee;
	private String cardNo;
	private String cardType;
	
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

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
