package com.everhomes.rest.organization.pm;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

public class UpdateOrganizationMemberByIdsCommand {
	
	@NotNull
	@ItemType(value = Long.class)
	private List<Long> ids;
	
	@NotNull
	private Long orgId;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
