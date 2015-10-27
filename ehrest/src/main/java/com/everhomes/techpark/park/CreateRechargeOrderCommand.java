package com.everhomes.techpark.park;

import javax.validation.constraints.NotNull;


/**
 * amount: 金额
 * months: 月数
 * plateNumber: 车牌号
 * ownerName: 车主名
 * validityPeriod: 有效期
 * 
 * @author xiongying
 *
 */
public class CreateRechargeOrderCommand {
	
	@NotNull
	private Integer amount;
	@NotNull
	private Byte months;
	@NotNull
	private String plateNumber;
	@NotNull
	private String ownerName;
	@NotNull
	private String validityPeriod;

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Byte getMonths() {
		return months;
	}

	public void setMonths(Byte months) {
		this.months = months;
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

}
