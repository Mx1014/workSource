package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>bills：订单列表</li> 
 * </ul>
 */
public class BatchCompleteBillCommandResponse {
    @ItemType(RentalBillDTO.class)
	private List<RentalBillDTO> bills;
    
	public List<RentalBillDTO> getBills() {
		return bills;
	}
	
	public void setBills(List<RentalBillDTO> bills) {
		this.bills = bills;
	}
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
