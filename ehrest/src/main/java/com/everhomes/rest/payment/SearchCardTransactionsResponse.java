package com.everhomes.rest.payment;

import java.util.List;

import com.everhomes.discover.ItemType;

public class SearchCardTransactionsResponse {
	private Long nextPageAnchor;
	@ItemType(CardTransactionDTO.class)
	private List<CardTransactionDTO> requests;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<CardTransactionDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CardTransactionDTO> requests) {
		this.requests = requests;
	}
	
	
}
