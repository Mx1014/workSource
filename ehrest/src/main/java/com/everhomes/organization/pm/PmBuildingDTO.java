package com.everhomes.organization.pm;

/**
 * 
 * pmBuildingId: 物业楼栋表的id
 * buildingName: 楼栋名
 * organizationId: 物业机构id
 *
 */
public class PmBuildingDTO {
	
	private Long pmBuildingId;
	
	private String buildingName;
	
	private Long organizationId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
