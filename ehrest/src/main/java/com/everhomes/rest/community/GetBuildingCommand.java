package com.everhomes.rest.community;

import javax.validation.constraints.NotNull;

/**
 * 
 * buildingId:楼栋id
 *
 */
public class GetBuildingCommand {
	
	@NotNull
	private Long buildingId;

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	
	

}
