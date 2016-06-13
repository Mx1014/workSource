package com.everhomes.rest.organization.pm;

/**
 * 
 * orgId: 机构id
 *
 */
public class ListPmBuildingCommand {
	
	private Long orgId;
	
	private Integer namespaceId;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	
}
