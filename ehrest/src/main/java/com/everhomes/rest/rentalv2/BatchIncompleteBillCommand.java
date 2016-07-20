package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>rentalBillIds：List<Long>  场所ID列表</li> 
 * </ul>
 */
public class BatchIncompleteBillCommand {

 
	@ItemType(Long.class)
	private List<Long> rentalBillIds;
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 

	  


 



	public List<Long> getRentalBillIds() {
		return rentalBillIds;
	}




	public void setRentalBillIds(List<Long> rentalBillIds) {
		this.rentalBillIds = rentalBillIds;
	}


 

 



}
