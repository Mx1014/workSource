// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

public class ImportAuthorizePriceDataDTO {
	private String apartmentName;
	private String status;
	private String chargeArea;
	private String chargingItemsName;
	private String authorizePrice;
	private String apartmentAuthorizeType;
	private String namespaceAddressType;
	private String namespaceAddressToken;

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

	public String getChargingItemsName() {
		return chargingItemsName;
	}

	public void setChargingItemsName(String chargingItemsName) {
		this.chargingItemsName = chargingItemsName;
	}

	public String getAuthorizePrice() {
		return authorizePrice;
	}

	public void setAuthorizePrice(String authorizePrice) {
		this.authorizePrice = authorizePrice;
	}

	public String getApartmentAuthorizeType() {
		return apartmentAuthorizeType;
	}

	public void setApartmentAuthorizeType(String apartmentAuthorizeType) {
		this.apartmentAuthorizeType = apartmentAuthorizeType;
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
