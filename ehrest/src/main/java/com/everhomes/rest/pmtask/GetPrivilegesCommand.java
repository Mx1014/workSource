package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class GetPrivilegesCommand {
	private Long organizationId;
	private Long communityId; 
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
}
