package com.everhomes.rest.payment;

import java.util.List;

import com.everhomes.discover.ItemType;

public class SearchCardTransactionsResponse {
	private Long nextPageAnchor;
	@ItemType(CardTransactionsDTO.class)
	private List<CardTransactionsDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<CardTransactionsDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CardTransactionsDTO> requests) {
		this.requests = requests;
	}
	
	
}
