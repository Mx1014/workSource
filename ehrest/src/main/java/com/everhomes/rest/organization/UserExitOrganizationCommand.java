package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 *	<ul>
 *	<li>communityId ： 小区id</li>
 *	<li>organizationType : 组织类型 ,详情{@link com.everhomes.rest.organization.OrganizationMemberStatus}</li>
 *	</ul>
 *
 */
public class UserExitOrganizationCommand {
	@NotNull
	private Long communityId;
	@NotNull
	private String organizationType;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
