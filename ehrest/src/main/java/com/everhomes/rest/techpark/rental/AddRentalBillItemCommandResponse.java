package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;

public class AddRentalBillItemCommandResponse {
	private String orderNo;
	private java.math.BigDecimal amount;
	private String name;
	private String description;
	private String orderType;
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
	 
	public java.math.BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(java.math.BigDecimal amount) {
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	} 
}
