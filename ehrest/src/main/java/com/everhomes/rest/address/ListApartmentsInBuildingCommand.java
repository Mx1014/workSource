package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>buildingId:楼宇id</li>
 *  <li>livingStatus:房源状态</li>
 * </ul>
 */
public class ListApartmentsInBuildingCommand {
	
	private Long buildingId;
	private Byte livingStatus;
	
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public Byte getLivingStatus() {
		return livingStatus;
	}
	public void setLivingStatus(Byte livingStatus) {
		this.livingStatus = livingStatus;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
