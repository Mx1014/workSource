package com.everhomes.rest.community;

import java.util.List;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListCommunitiesByKeywordCommandResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(CommunityDTO.class)
	private List<CommunityDTO> requests;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CommunityDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<CommunityDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
