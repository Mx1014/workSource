package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchStatisticsDTO;
import com.everhomes.util.StringHelper;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>rentalBills：订单列表</li>
 * <li>nextPageAnchor：下页锚点</li>
 * </ul>
 */
public class ListRentalBillsCommandResponse {

	private Long nextPageAnchor;
	private Long totalNum;
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

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}
}
