package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;

public class AddRentalBillItemCommandResponse {
	private String orderNo;
	private Double amount;
	private String name;
	private String description;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	} 
}
