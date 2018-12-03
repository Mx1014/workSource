package com.everhomes.rest.openapi;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListAddressesForThirdPartyResponse {
	
	private List<ApartmentDTO> results;
	private Long nextPageAnchor;
	
	public List<ApartmentDTO> getResults() {
		return results;
	}
	public void setResults(List<ApartmentDTO> results) {
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
