package com.everhomes.rest.rentalv2;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>rentalBills: 订单列表 {@link com.everhomes.rest.rentalv2.RentalBriefOrderDTO}</li>
 * </ul>
 */
public class ListRentalOrdersResponse {

	private Long nextPageAnchor;
	@ItemType(RentalBriefOrderDTO.class)
	private List<RentalBriefOrderDTO> rentalBills;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<RentalBriefOrderDTO> getRentalBills() {
		return rentalBills;
	}

	public void setRentalBills(List<RentalBriefOrderDTO> rentalBills) {
		this.rentalBills = rentalBills;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}
