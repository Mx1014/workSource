package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationBillingTransactionDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 *  <li>nextPageOffset : 下一页页码</li>
 *	<li>requests : 缴费记录列表,详见: {@link com.everhomes.rest.organization.OrganizationBillingTransactionDTO}</li>
 *</ul>
 *
 */

public class ListOrgBillingTransactionsByConditionsCommandResponse {
	
	private Long nextPageOffset;
	
	@ItemType(OrganizationBillingTransactionDTO.class)
	private List<OrganizationBillingTransactionDTO> requests;

	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<OrganizationBillingTransactionDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<OrganizationBillingTransactionDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
