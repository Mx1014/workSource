package com.everhomes.rest.address.admin;

import com.everhomes.util.StringHelper;

/**
 * 楼栋信息
 * <ul>
 *  <li>communityId:园区id</li>
 * </ul>
 */
public class ImportAddressCommand {
	private Long communityId;
	private Long buildingId;
	

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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
