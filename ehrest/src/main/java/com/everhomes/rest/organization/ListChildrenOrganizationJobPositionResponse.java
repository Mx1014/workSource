package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListChildrenOrganizationJobPositionResponse {
	private Long nextPageAnchor;
	
	@ItemType(ChildrenOrganizationJobPositionDTO.class)
	private List<ChildrenOrganizationJobPositionDTO> requests;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ChildrenOrganizationJobPositionDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<ChildrenOrganizationJobPositionDTO> requests) {
		this.requests = requests;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
