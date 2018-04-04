package com.everhomes.rest.enterprise;

public class ImportEnterpriseDataCommand {
	private Long manageOrganizationId;

	private Integer namespaceId;
	
	private Long communityId;

	public Long getManageOrganizationId() {
		return manageOrganizationId;
	}

	public void setManageOrganizationId(Long manageOrganizationId) {
		this.manageOrganizationId = manageOrganizationId;
	}

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

	
}
