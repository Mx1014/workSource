package com.everhomes.techpark.park;

import javax.validation.constraints.NotNull;


public class CreateParkingChargeCommand {
	
	@NotNull
	private Byte months;
	
	@NotNull
	private Double amount;
	
	private Long enterpriseCommunityId;

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

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}
	
	
}
