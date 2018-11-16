package com.everhomes.rest.openapi;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListBuildingsForThirdPartyResponse {
	
	private List<BuildingDTO> results;
	private Long nextPageAnchor;
	
	public List<BuildingDTO> getResults() {
		return results;
	}
	public void setResults(List<BuildingDTO> results) {
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
