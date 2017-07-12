package com.everhomes.rest.openapi.techpark;

import com.everhomes.util.StringHelper;

public class CustomerApartment {
	private String buildingName;
	private String apartmentName;
	private String apartmentFloor;
	private Double areaSize;
	private Double rentArea;
	private Double buildArea;
	private Double innerArea;
	private String layout;
	private Byte livingStatus;
	private Boolean dealed;

	public CustomerApartment() {
		super();
	}

	public CustomerApartment(String buildingName, String apartmentName) {
		super();
		this.buildingName = buildingName;
		this.apartmentName = apartmentName;
	}

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

	public String getApartmentFloor() {
		return apartmentFloor;
	}

	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public Double getRentArea() {
		return rentArea;
	}

	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
	}

	public Double getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(Double buildArea) {
		this.buildArea = buildArea;
	}

	public Double getInnerArea() {
		return innerArea;
	}

	public void setInnerArea(Double innerArea) {
		this.innerArea = innerArea;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
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
