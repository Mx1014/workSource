package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListPmBillsByConditionsCommandResponse {
	
	private Long nextPageAnchor;
	
	private List<PmBillsDTO> requests;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
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
