package com.everhomes.rest.techpark.park;

import java.sql.Timestamp;

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
	private Double amount;
	@NotNull
	private Byte months;
	@NotNull
	private String plateNumber;
	@NotNull
	private String ownerName;
	@NotNull
	private String validityPeriod;

	private String cardType;
	private Long communityId;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
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

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

}
