// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

public class ImportApartmentDataDTO {
	private String buildingName;
	private String apartmentName;
	private String status;
	private String areaSize;
	private String namespaceAddressType;
	private String namespaceAddressToken;

	public String getNamespaceAddressToken() {
		return namespaceAddressToken;
	}

	public void setNamespaceAddressToken(String namespaceAddressToken) {
		this.namespaceAddressToken = namespaceAddressToken;
	}

	public String getNamespaceAddressType() {
		return namespaceAddressType;
	}

	public void setNamespaceAddressType(String namespaceAddressType) {
		this.namespaceAddressType = namespaceAddressType;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
