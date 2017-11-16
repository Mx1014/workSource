package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchStatisticsDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 订单DTO
 * <li>nextPageAnchor：下页锚点</li>
 * <li>rentalBills：List<RentalBillDTO> 订单列表</li> 
 * </ul>
 */
public class FindRentalBillsCommandResponse {

    private Long nextPageAnchor;
    @ItemType(RentalBillDTO.class)
	private List<RentalBillDTO> rentalBills;
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }
	public List<RentalBillDTO> getRentalBills() {
		return rentalBills;
	}
	public void setRentalBills(List<RentalBillDTO> rentalBills) {
		this.rentalBills = rentalBills;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	} 
}
