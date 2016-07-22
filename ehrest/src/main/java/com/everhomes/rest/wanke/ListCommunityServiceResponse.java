package com.everhomes.rest.wanke;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListCommunityServiceResponse {
	
	@ItemType(CommunityServiceDTO.class)
	private List<CommunityServiceDTO> requests;
	
	private Long nextPageAnchor;

	public List<CommunityServiceDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<CommunityServiceDTO> requests) {
		this.requests = requests;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
