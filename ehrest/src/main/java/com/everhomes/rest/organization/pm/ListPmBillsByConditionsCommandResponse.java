package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * 
 * <ul>
 * <li>nextPageOffset : 页码</li>
 * <li>requests : 账单列表,详见: {@link com.everhomes.rest.organization.pm.PmBillsDTO}</li>
 * </ul>
 *
 */

public class ListPmBillsByConditionsCommandResponse {

	private Long nextPageOffset;

	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> requests;

	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<PmBillsDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<PmBillsDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}



}
