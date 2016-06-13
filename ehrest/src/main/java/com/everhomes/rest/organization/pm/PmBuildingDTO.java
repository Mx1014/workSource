package com.everhomes.rest.organization.pm;

/**
 * 
 * buildingId: 楼栋id
 * buildingName: 楼栋名
 *
 */
public class PmBuildingDTO {
	
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
