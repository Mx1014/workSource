package com.bosigao.cxf.rest;

import com.everhomes.util.StringHelper;

public class BosigaoCardInfo {
	private String userName;
	private String mobile;
	private String carNumber;
	private Boolean valid;
	private String validEnd;
	private String cost;
	private String cardDescript;
	private String cardCode;
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
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getValidEnd() {
		return validEnd;
	}
	public void setValidEnd(String validEnd) {
		this.validEnd = validEnd;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getCardDescript() {
		return cardDescript;
	}
	public void setCardDescript(String cardDescript) {
		this.cardDescript = cardDescript;
	}
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
