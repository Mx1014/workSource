package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

public class ApartmentManagementPrivilegeCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Long organizationId;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
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
