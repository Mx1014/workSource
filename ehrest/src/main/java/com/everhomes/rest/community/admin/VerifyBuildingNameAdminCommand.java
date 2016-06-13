package com.everhomes.rest.community.admin;


/**
 * <ul>
 *  <li>communityId: 小区id</li>
 *  <li>buildingName: 楼栋名称</li>
 * </ul>
 *
 */
public class VerifyBuildingNameAdminCommand {
	
	private Long communityId;
	
	private String buildingName;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

}
