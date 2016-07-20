package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态 
 * <li>rentalBillId：订单ID</li> 
 * </ul>
 */
public class CancelRentalBillCommand { 
	@NotNull
	private Long rentalBillId; 
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 
 
	public Long getRentalBillId() {
		return rentalBillId;
	}



	public void setRentalBillId(Long rentalBillId) {
		this.rentalBillId = rentalBillId;
	}
 
}
