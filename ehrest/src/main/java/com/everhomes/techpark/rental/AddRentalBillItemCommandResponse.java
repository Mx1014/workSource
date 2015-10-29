package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;

public class AddRentalBillItemCommandResponse {
	private String orderNo;
	private Double amount;
	private String name;
	private String des1cription;
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
	public String getDes1cription() {
		return des1cription;
	}
	public void setDes1cription(String des1cription) {
		this.des1cription = des1cription;
	}
}
