package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 管理公司id</li>
 *     <li>communityId: 园区Id，不填则是默认方案</li>
 * </ul>
 */
public class ListAppCommunityConfigsCommand {
	private Long communityId;
	private Long organizationId;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
