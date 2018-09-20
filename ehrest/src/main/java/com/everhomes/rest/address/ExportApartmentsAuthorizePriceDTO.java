package com.everhomes.rest.address;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class ExportApartmentsAuthorizePriceDTO {

	private String apartmentName;
	private String livingStatus;
	private Double chargeArea;
	private String chargingItemsName;
	private BigDecimal authorizePrice;
	private String apartmentAuthorizeType;
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

	public Double getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}

	public String getChargingItemsName() {
		return chargingItemsName;
	}

	public void setChargingItemsName(String chargingItemsName) {
		this.chargingItemsName = chargingItemsName;
	}

	public BigDecimal getAuthorizePrice() {
		return authorizePrice;
	}

	public void setAuthorizePrice(BigDecimal authorizePrice) {
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
