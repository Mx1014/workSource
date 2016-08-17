package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchTaskStatisticsResponse {
	private Long nextPageAnchor;
	@ItemType(TaskStatisticsDTO.class)
	private List<TaskStatisticsDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<TaskStatisticsDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<TaskStatisticsDTO> requests) {
		this.requests = requests;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
