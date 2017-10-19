package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>organizationId: organizationId</li>
 * </ul>
 */
public class CountCommunityUsersCommand {

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
