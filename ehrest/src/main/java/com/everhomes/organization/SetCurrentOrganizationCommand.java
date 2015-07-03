package com.everhomes.organization;

import javax.validation.constraints.NotNull;

public class SetCurrentOrganizationCommand {
    @NotNull
    private Long organizationId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

   

}
