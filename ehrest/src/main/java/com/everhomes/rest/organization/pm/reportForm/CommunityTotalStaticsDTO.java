package com.everhomes.rest.organization.pm.reportForm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class CommunityTotalStaticsDTO {
	
	private Integer communityCount;
	private Integer buildingCount;
	private BigDecimal areaSize;
	
	public Integer getCommunityCount() {
		return communityCount;
	}
	public void setCommunityCount(Integer communityCount) {
		this.communityCount = communityCount;
	}
	public Integer getBuildingCount() {
		return buildingCount;
	}
	public void setBuildingCount(Integer buildingCount) {
		this.buildingCount = buildingCount;
	}
	public BigDecimal getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(BigDecimal areaSize) {
		this.areaSize = areaSize;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
