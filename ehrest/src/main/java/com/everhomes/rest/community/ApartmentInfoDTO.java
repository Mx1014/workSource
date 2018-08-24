package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class ApartmentInfoDTO {
	
	private Long communityId;
	private Long addressId;
	private String communityName;
	private String buildingName;
	private String apartmentFloor;
	private String apartmentName;
	private Byte livingStatus;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private Double totalRent;
	private Double areaAveragePrice;
	private Integer floorNumber;
	private Integer relatedContractNumber;
	
	public Integer getRelatedContractNumber() {
		return relatedContractNumber;
	}
	public void setRelatedContractNumber(Integer relatedContractNumber) {
		this.relatedContractNumber = relatedContractNumber;
	}
	public Integer getFloorNumber() {
		return floorNumber;
	}
	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public Double getTotalRent() {
		return totalRent;
	}
	public void setTotalRent(Double totalRent) {
		this.totalRent = totalRent;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getApartmentFloor() {
		return apartmentFloor;
	}
	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public Byte getLivingStatus() {
		return livingStatus;
	}
	public void setLivingStatus(Byte livingStatus) {
		this.livingStatus = livingStatus;
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
	public Double getAreaAveragePrice() {
		return areaAveragePrice;
	}
	public void setAreaAveragePrice(Double areaAveragePrice) {
		this.areaAveragePrice = areaAveragePrice;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
