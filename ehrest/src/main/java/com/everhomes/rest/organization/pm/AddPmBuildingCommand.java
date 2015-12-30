package com.everhomes.rest.organization.pm;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 *  <li>organizationId：机构id</li>
 *  <li>buildingIds：楼栋id列表</li>
 *  <li>isAll：全部或部分 0-全部；1-部分</li>
 *  <li>communityId：小区id</li>
 * </ul>
 */
public class AddPmBuildingCommand {
	
	private int isAll;

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

	public int getIsAll() {
		return isAll;
	}

	public void setIsAll(int isAll) {
		this.isAll = isAll;
	}

}
