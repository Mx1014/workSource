package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class GetPrivilegesCommand {
	private Long organizationId;

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
}
