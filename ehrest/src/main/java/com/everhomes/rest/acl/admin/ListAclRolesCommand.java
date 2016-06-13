package com.everhomes.rest.acl.admin;

/**
 * <ul>
 * <li>appId:园区app的标示id</li>
 * <li>organizationId: 机构id</li>
 * </ul>
 */
public class ListAclRolesCommand {
	
	private Long appId;
	
	private Long organizationId;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
