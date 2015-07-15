package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListPmBillsByConditionsCommandResponse {
	
	private Long nextPageAnchor;
	
	private List<PmBillsDto> requests;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PmBillsDto> getRequests() {
		return requests;
	}

	public void setRequests(List<PmBillsDto> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
