package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class BuildingStatisticsDTO {
	
	private Long buildingId;
	private String buildingName;
	private String address;
	private Integer apartmentNumber;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private Integer relatedContractNumber;
	private Double areaAveragePrice;
	private Integer relatedEnterpriseCustomerNumber;
	private Integer relatedOrganizationOwnerNumber;
	
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getApartmentNumber() {
		return apartmentNumber;
	}
	public void setApartmentNumber(Integer apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
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
	public Integer getRelatedContractNumber() {
		return relatedContractNumber;
	}
	public void setRelatedContractNumber(Integer relatedContractNumber) {
		this.relatedContractNumber = relatedContractNumber;
	}
	public Double getAreaAveragePrice() {
		return areaAveragePrice;
	}
	public void setAreaAveragePrice(Double areaAveragePrice) {
		this.areaAveragePrice = areaAveragePrice;
	}
	public Integer getRelatedEnterpriseCustomerNumber() {
		return relatedEnterpriseCustomerNumber;
	}
	public void setRelatedEnterpriseCustomerNumber(Integer relatedEnterpriseCustomerNumber) {
		this.relatedEnterpriseCustomerNumber = relatedEnterpriseCustomerNumber;
	}
	public Integer getRelatedOrganizationOwnerNumber() {
		return relatedOrganizationOwnerNumber;
	}
	public void setRelatedOrganizationOwnerNumber(Integer relatedOrganizationOwnerNumber) {
		this.relatedOrganizationOwnerNumber = relatedOrganizationOwnerNumber;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
