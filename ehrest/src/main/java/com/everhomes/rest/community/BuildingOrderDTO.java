package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class BuildingOrderDTO {
	
	private Long buildingId;
	private Long defaultOrder;
	
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public Long getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Long defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
