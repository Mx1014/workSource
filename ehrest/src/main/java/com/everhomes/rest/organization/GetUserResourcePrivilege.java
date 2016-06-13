package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

public class GetUserResourcePrivilege {

	@NotNull
	private Long organizationId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
