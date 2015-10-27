package com.everhomes.techpark.park;

import javax.validation.constraints.NotNull;


public class CreateParkingChargeCommand {
	
	@NotNull
	private Byte months;
	
	@NotNull
	private Integer amount;

	public Byte getMonths() {
		return months;
	}

	public void setMonths(Byte months) {
		this.months = months;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
