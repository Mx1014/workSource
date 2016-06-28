package com.everhomes.rest.payment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchCardUsersResponse {
	
	private Long nextPageAnchor;
	@ItemType(CardUserDTO.class)
	private List<CardUserDTO> requests;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CardUserDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<CardUserDTO> requests) {
		this.requests = requests;
	}
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
