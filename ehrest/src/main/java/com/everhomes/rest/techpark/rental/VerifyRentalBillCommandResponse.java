package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;

public class VerifyRentalBillCommandResponse {
	private Byte addBillCode; 
	private RentalBillDTO rentalBill;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Byte getAddBillCode() {
		return addBillCode;
	}
	public void setAddBillCode(Byte addBillCode) {
		this.addBillCode = addBillCode;
	}
	public RentalBillDTO getRentalBill() {
		return rentalBill;
	}
	public void setRentalBill(RentalBillDTO rentalBill) {
		this.rentalBill = rentalBill;
	} 
}
