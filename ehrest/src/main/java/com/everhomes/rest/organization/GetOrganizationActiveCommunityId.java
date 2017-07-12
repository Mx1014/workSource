package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId : 组织id</li>
 * </ul>
 */
public class GetOrganizationActiveCommunityId {
	
	private Long organizationId;

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
