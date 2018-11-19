package com.everhomes.rest.openapi;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListCommunitiesForThirdPartyResponse {
	
	private List<CommunityDTO> results;
	private Long nextPageAnchor;
	
	public List<CommunityDTO> getResults() {
		return results;
	}
	public void setResults(List<CommunityDTO> results) {
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
