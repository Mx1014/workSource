package com.everhomes.organization.pm;

import com.everhomes.util.StringHelper;

public class ListApartmentsForAppCommand {
	
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
