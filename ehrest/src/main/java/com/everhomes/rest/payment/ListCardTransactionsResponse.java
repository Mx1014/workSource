package com.everhomes.rest.payment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListCardTransactionsResponse {
	private Long nextPageAnchor;
	@ItemType(CardTransactionOfMonth.class)
	private List<CardTransactionOfMonth> requests;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	public List<CardTransactionOfMonth> getRequests() {
		return requests;
	}
	public void setRequests(List<CardTransactionOfMonth> requests) {
		this.requests = requests;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
