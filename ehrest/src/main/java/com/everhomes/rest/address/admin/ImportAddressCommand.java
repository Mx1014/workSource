package com.everhomes.rest.address.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: 项目编号</li>
 *     <li>name: 楼栋名称</li>
 * </ul>
 */
public class ImportAddressCommand {
	private Long communityId;
	private Long buildingId;

    private String name;

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
