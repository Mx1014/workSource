package com.everhomes.rest.payment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchCardRechargeOrderResponse {
	private Long nextPageAnchor;
	@ItemType(CardRechargeOrderDTO.class)
	private List<CardRechargeOrderDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<CardRechargeOrderDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CardRechargeOrderDTO> requests) {
		this.requests = requests;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
