package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchTaskCategoryStatisticsResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(TaskCategoryStatisticsDTO.class)
	private List<TaskCategoryStatisticsDTO> requests;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<TaskCategoryStatisticsDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<TaskCategoryStatisticsDTO> requests) {
		this.requests = requests;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
