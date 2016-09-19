package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchTasksResponse {
	private Long nextPageAnchor;
	@ItemType(PmTaskDTO.class)
	private List<PmTaskDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<PmTaskDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<PmTaskDTO> requests) {
		this.requests = requests;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
