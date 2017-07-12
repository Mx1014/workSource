package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchTaskOperatorStatisticsResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(TaskOperatorStatisticsDTO.class)
	private List<TaskOperatorStatisticsDTO> requests;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	public List<TaskOperatorStatisticsDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<TaskOperatorStatisticsDTO> requests) {
		this.requests = requests;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
