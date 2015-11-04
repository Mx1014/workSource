package com.everhomes.techpark.park;

import javax.validation.constraints.NotNull;


public class CreateParkingChargeCommand {
	
	@NotNull
	private Byte months;
	
	@NotNull
	private Double amount;

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
	
}
