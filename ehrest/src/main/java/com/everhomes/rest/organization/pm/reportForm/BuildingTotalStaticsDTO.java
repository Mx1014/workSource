package com.everhomes.rest.organization.pm.reportForm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class BuildingTotalStaticsDTO {
	
	private Integer buildindCount;
	private Integer totalApartmentCount;
	private BigDecimal areaSize;
	
	public Integer getBuildindCount() {
		return buildindCount;
	}
	public void setBuildindCount(Integer buildindCount) {
		this.buildindCount = buildindCount;
	}
	public Integer getTotalApartmentCount() {
		return totalApartmentCount;
	}
	public void setTotalApartmentCount(Integer totalApartmentCount) {
		this.totalApartmentCount = totalApartmentCount;
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
