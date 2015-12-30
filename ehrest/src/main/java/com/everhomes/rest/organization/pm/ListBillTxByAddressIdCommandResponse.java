package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyBillingTransactionDTO;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>nextPageOffset : 下一页页码</li>
 *	<li>requests : 缴费记录列表,详见:{@link com.everhomes.rest.family.FamilyBillingTransactionDTO}</li>
 *</ul>
 *
 */
public class ListBillTxByAddressIdCommandResponse {
	
	private Long nextPageOffset;
	
	@ItemType(FamilyBillingTransactionDTO.class)
	private List<FamilyBillingTransactionDTO> requests;
	
	public Long getNextPageOffset() {
		return nextPageOffset;
	}
	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	public List<FamilyBillingTransactionDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<FamilyBillingTransactionDTO> requests) {
		this.requests = requests;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
