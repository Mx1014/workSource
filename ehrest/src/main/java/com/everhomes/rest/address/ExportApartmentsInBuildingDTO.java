package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

public class ExportApartmentsInBuildingDTO {
	
	private String apartmentName;
	private String livingStatus;
	private String apartmentFloor;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private String orientation;
	private String namespaceAddressType;
	private String namespaceAddressToken;
	
	
	public String getApartmentName() {
		return apartmentName;
	}


	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}


	public String getLivingStatus() {
		return livingStatus;
	}


	public void setLivingStatus(String livingStatus) {
		this.livingStatus = livingStatus;
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


	public Double getFreeArea() {
		return freeArea;
	}


	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}


	public Double getChargeArea() {
		return chargeArea;
	}


	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}


	public String getOrientation() {
		return orientation;
	}


	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}


	public String getNamespaceAddressType() {
		return namespaceAddressType;
	}


	public void setNamespaceAddressType(String namespaceAddressType) {
		this.namespaceAddressType = namespaceAddressType;
	}


	public String getNamespaceAddressToken() {
		return namespaceAddressToken;
	}


	public void setNamespaceAddressToken(String namespaceAddressToken) {
		this.namespaceAddressToken = namespaceAddressToken;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
