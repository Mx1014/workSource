package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class GetBuildingStatisticsCommand {
	
	private Long buildingId;

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
