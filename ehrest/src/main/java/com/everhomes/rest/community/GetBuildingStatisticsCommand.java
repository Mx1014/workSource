package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>buildingId: 楼栋id</li>
 * </ul>
 */
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
