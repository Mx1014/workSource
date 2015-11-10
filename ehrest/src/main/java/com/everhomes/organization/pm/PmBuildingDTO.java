package com.everhomes.organization.pm;

/**
 * 
 * pmBuildingId: 物业楼栋表的id
 * buildingName: 楼栋名
 * buildingId: 楼栋id
 *
 */
public class PmBuildingDTO {
	
	private Long pmBuildingId;
	
	private String buildingName;
	
	private Long buildingId;

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public Long getPmBuildingId() {
		return pmBuildingId;
	}

	public void setPmBuildingId(Long pmBuildingId) {
		this.pmBuildingId = pmBuildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	
}
