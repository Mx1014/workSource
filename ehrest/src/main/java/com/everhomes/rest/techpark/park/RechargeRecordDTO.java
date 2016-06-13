package com.everhomes.rest.techpark.park;

import java.sql.Timestamp;

public class RechargeRecordDTO {
	
	private String plateNumber;
	
	private String ownerName;
	
	private String rechargePhone;
	
	private Timestamp rechargeTime;
	
	private Byte rechargeMonth;
	
	private Double rechargeAmount;
	
	private Byte paymentStatus;
	
	private Byte rechargeStatus;
	
	private Timestamp validityperiod;
	
	public Timestamp getValidityperiod() {
		return validityperiod;
	}

	public void setValidityperiod(Timestamp validityperiod) {
		this.validityperiod = validityperiod;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getRechargePhone() {
		return rechargePhone;
	}

	public void setRechargePhone(String rechargePhone) {
		this.rechargePhone = rechargePhone;
	}

	public Timestamp getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Timestamp rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Byte getRechargeMonth() {
		return rechargeMonth;
	}

	public void setRechargeMonth(Byte rechargeMonth) {
		this.rechargeMonth = rechargeMonth;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Byte getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Byte paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Byte getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(Byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}
	
}
