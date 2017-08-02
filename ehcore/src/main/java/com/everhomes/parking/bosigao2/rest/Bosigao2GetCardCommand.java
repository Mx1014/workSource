package com.bosigao2.rest;

import com.everhomes.util.StringHelper;

public class Bosigao2GetCardCommand {
	private String clientID;
	private String cardCode;
	private String plateNo;
	private String flag;
	
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
