package com.everhomes.techpark.park;

import javax.validation.constraints.NotNull;

public class DeleteParkingChargeCommand {
	
	@NotNull
	private Long id;
	
	@NotNull
	private Byte months;
	
	@NotNull
	private Double amount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
