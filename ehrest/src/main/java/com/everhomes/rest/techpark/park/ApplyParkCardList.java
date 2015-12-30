package com.everhomes.rest.techpark.park;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ApplyParkCardList {

	@ItemType(value = ApplyParkCardDTO.class)
	private List<ApplyParkCardDTO> applyCard;
	
	private Long nextPageAnchor;

	public List<ApplyParkCardDTO> getApplyCard() {
		return applyCard;
	}

	public void setApplyCard(List<ApplyParkCardDTO> applyCard) {
		this.applyCard = applyCard;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
}
