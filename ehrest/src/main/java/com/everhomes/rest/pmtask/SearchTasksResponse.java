package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchTasksResponse {
	private Long nextPageAnchor;
	@ItemType(CommunityTaskDTO.class)
	private List<CommunityTaskDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<CommunityTaskDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CommunityTaskDTO> requests) {
		this.requests = requests;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
