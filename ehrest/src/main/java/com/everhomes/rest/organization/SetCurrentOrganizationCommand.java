package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;
/**
 * <ul>
 * 	<li>organizationId : 组织id</li>
 *</ul>
 *
 */
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
