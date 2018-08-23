package com.everhomes.rest.community;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ChangeBuildingOrderCommand {
	
	private List<BuildingOrderDTO> buildingOrders;

	public List<BuildingOrderDTO> getBuildingOrders() {
		return buildingOrders;
	}

	public void setBuildingOrders(List<BuildingOrderDTO> buildingOrders) {
		this.buildingOrders = buildingOrders;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
