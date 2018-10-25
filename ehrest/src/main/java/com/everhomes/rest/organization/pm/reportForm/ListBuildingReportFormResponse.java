package com.everhomes.rest.organization.pm.reportForm;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListBuildingReportFormResponse {
	
	private List<BuildingReportFormDTO> results;
	private Long nextPageAnchor;
	
	public List<BuildingReportFormDTO> getResults() {
		return results;
	}
	public void setResults(List<BuildingReportFormDTO> results) {
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
