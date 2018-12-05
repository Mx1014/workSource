package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListApartmentEventsResponse {
	
	private List<ApartmentEventDTO> results;
	private Long nextPageAnchor;
	
	public List<ApartmentEventDTO> getResults() {
		return results;
	}
	public void setResults(List<ApartmentEventDTO> results) {
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
