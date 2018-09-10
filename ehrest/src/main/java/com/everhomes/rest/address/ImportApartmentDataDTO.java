// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

public class ImportApartmentDataDTO {
	//private String buildingName;
	//private String buildArea;
	//private String sharedArea;
	private String apartmentName;
	private String status;
	private String apartmentFloor;
	private String areaSize;
	private String rentArea;
	private String freeArea;
	private String chargeArea;
	private String orientation;
	private String namespaceAddressType;
	private String namespaceAddressToken;
	
	public String getApartmentFloor() {
		return apartmentFloor;
	}

	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}

	public String getFreeArea() {
		return freeArea;
	}

	public void setFreeArea(String freeArea) {
		this.freeArea = freeArea;
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

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(String chargeArea) {
		this.chargeArea = chargeArea;
	}

	public String getRentArea() {
		return rentArea;
	}

	public void setRentArea(String rentArea) {
		this.rentArea = rentArea;
	}

	public String getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(String areaSize) {
		this.areaSize = areaSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
