package com.everhomes.organization.pm;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 *  <li>organizationId：机构id</li>
 *  <li>buildingIds：楼栋id列表</li>
 *  <li>isAll：全部或部分</li>
 *  <li>communityId：小区id</li>
 * </ul>
 */
public class AddPmBuildingCommand {
	
	private Boolean isAll;

	private Long communityId;
	@NotNull
	private Long organizationId;
	
	@NotNull
	@ItemType(Long.class)
	private List<Long> buildingIds;

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

	public List<Long> getBuildingIds() {
		return buildingIds;
	}

	public void setBuildingIds(List<Long> buildingIds) {
		this.buildingIds = buildingIds;
	}

	public Boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}
	
}
