package com.everhomes.rest.community.admin;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * <ul>
 * <li>buildingId: 被审核楼栋Id</li>
 * </ul>
 */
public class VerifyBuildingAdminCommand {

	@NotNull
    private Long buildingId;

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	
	
	
}
