package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *  <li>buildingId: 楼栋id</li>
 *  <li>buildingName: 楼栋名</li>
 * </ul>
 *
 */
public class UnassignedBuildingDTO {

	private Long buildingId;
	
	private String buildingName;

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
}
