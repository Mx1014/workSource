package com.everhomes.rest.techpark.park;

import javax.validation.constraints.NotNull;


public class CreateParkingChargeCommand {
	
	@NotNull
	private Byte months;
	
	@NotNull
	private Double amount;

	private String cardType;
	private Long communityId;

	public Byte getMonths() {
		return months;
	}

	public void setMonths(Byte months) {
		this.months = months;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	
}
