package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.pm.ChildrenOrganizationJobLevelDTO;
import com.everhomes.util.StringHelper;

public class ListChildrenOrganizationJobLevelResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(ChildrenOrganizationJobLevelDTO.class)
	private List<ChildrenOrganizationJobLevelDTO> requests;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ChildrenOrganizationJobLevelDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<ChildrenOrganizationJobLevelDTO> requests) {
		this.requests = requests;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
