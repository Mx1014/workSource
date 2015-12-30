// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>organizationId：组织id</li>
 * <li>communityIds：小区id列表</li>
 * </ul>
 */
public class DeleteOrganizationCommunityCommand {
	@NotNull
	private Long organizationId;
	@NotNull
	@ItemType(Long.class)
	private List<Long> communityIds;

	
	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	public List<Long> getCommunityIds() {
		return communityIds;
	}



	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
