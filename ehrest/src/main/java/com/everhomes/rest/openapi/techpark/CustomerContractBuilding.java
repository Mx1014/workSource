package com.everhomes.rest.openapi.techpark;

import com.everhomes.util.StringHelper;

public class CustomerContractBuilding {
	private String buildingName;
	private String apartmentName;
	private Double areaSize;
	private Boolean dealed;

	public Boolean getDealed() {
		return dealed;
	}

	public void setDealed(Boolean dealed) {
		this.dealed = dealed;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
