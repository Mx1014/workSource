package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListOweFamilysByConditionCommandResponse {
	
private Long nextPageOffset;

	@ItemType(OweFamilyDTO.class)
	private List<OweFamilyDTO> requests;

	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<OweFamilyDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<OweFamilyDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
