package com.everhomes.rest.community;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListBuildingsForAppResponse {

	private List<BuildingStatisticsForAppDTO> results;
	private Long nextPageAnchor;
	
	public List<BuildingStatisticsForAppDTO> getResults() {
		return results;
	}
	public void setResults(List<BuildingStatisticsForAppDTO> results) {
		this.results = results;
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
