package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>rentalSiteId：场所ID</li> 
 * </ul>
 */
public class IncompleteBillCommand {


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
