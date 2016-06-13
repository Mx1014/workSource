package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>统计返回值 
 * <li>successCount：已预约总数</li> 
 * </ul>
 */
public class ListRentalBillCountCommandResponse {
	@ItemType(RentalBillCountDTO.class)
	private List<RentalBillCountDTO> rentalBillCounts;
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }
	public List<RentalBillCountDTO> getRentalBillCounts() {
		return rentalBillCounts;
	}
	public void setRentalBillCounts(List<RentalBillCountDTO> rentalBillCounts) {
		this.rentalBillCounts = rentalBillCounts;
	}

}
