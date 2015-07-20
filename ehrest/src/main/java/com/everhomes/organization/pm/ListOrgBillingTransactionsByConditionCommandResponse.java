package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.organization.OrganizationBillingTransactions;
import com.everhomes.util.StringHelper;

public class ListOrgBillingTransactionsByConditionCommandResponse {
	
	private Long nextPageOffset;
	
	@ItemType(OrganizationBillingTransactions.class)
	private List<OrganizationBillingTransactions> requests;

	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<OrganizationBillingTransactions> getRequests() {
		return requests;
	}

	public void setRequests(List<OrganizationBillingTransactions> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
