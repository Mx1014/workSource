package com.everhomes.rest.pmsy;

import java.util.List;

import com.everhomes.discover.ItemType;

public class SearchBillsOrdersResponse {
	private Long nextPageAnchor;
	@ItemType(PmBillsOrdersDTO.class)
    private List<PmBillsOrdersDTO> requests;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<PmBillsOrdersDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<PmBillsOrdersDTO> requests) {
		this.requests = requests;
	}
	
	
}
