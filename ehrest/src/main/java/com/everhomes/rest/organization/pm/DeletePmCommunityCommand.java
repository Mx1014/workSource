package com.everhomes.rest.organization.pm;


/**
 * 
 * <ul>
 *	<li>organizationId :机构Id</li>
 *	<li>communityId :小区Id</li>
 *</ul>
 *
 */
public class DeletePmCommunityCommand {
	
	private Long organizationId;
	
	private Long communityId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	

}
