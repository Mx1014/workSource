package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.util.StringHelper;

public class ListOperatePersonnelsResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(OrganizationMemberDTO.class)
    private List<OrganizationMemberDTO> members;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<OrganizationMemberDTO> getMembers() {
		return members;
	}

	public void setMembers(List<OrganizationMemberDTO> members) {
		this.members = members;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
