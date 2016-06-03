package com.everhomes.rest.payment;

import java.util.List;

import com.everhomes.discover.ItemType;

public class SearchCardTranscationsResponse {
	private Long nextPageAnchor;
	@ItemType(CardTranscationsDTO.class)
	private List<CardTranscationsDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<CardTranscationsDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CardTranscationsDTO> requests) {
		this.requests = requests;
	}
	
	
}
