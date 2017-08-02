package com.everhomes.parking.bosigao2.rest;

import com.everhomes.util.StringHelper;

public class Bosigao2RechargeCommand {
	private String clientID;
	private String cardCode;
	private String plateNo;
	private String flag;
	private String payMos;
	private String amount;
	private String payDate;
	private String chargePaidNo;
	
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
	public String getPayMos() {
		return payMos;
	}
	public void setPayMos(String payMos) {
		this.payMos = payMos;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getChargePaidNo() {
		return chargePaidNo;
	}
	public void setChargePaidNo(String chargePaidNo) {
		this.chargePaidNo = chargePaidNo;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
