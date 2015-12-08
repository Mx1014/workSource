package com.everhomes.techpark.rental;

import java.util.List;

import com.everhomes.util.StringHelper;

public class BatchCompleteBillCommandResponse {
	private List<RentalBillDTO> bills;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public List<RentalBillDTO> getBills() {
		return bills;
	}
	public void setBills(List<RentalBillDTO> bills) {
		this.bills = bills;
	}
 

}
