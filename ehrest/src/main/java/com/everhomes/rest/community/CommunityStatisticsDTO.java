package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>communityName: 园区名称</li>
 *     <li>address: 地址</li>
 *     <li>buildingNumber: 园区下的楼栋数量</li>
 *     <li>apartmentNumber: 园区下的门牌数量</li>
 *     <li>areaSize: 建筑面积</li>
 *     <li>rentArea: 在租面积</li>
 *     <li>freeArea: 可招租面积</li>
 *     <li>chargeArea: 收费面积</li>
 *     <li>relatedContractNumber: 在租合同数</li>
 *     <li>areaAveragePrice: 在租实时均价（元/平方米）</li>
 *     <li>totalRent: 在租合同总金额</li>
 * </ul>
 */
public class CommunityStatisticsDTO {
	
	private Long communityId;
	private String communityName;
	private String address;
	private Integer buildingNumber;
	private Integer apartmentNumber;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private Integer relatedContractNumber;
	private Double areaAveragePrice;
	private Double totalRent;
	
	public Double getTotalRent() {
		return totalRent;
	}
	public void setTotalRent(Double totalRent) {
		this.totalRent = totalRent;
	}
	public Integer getApartmentNumber() {
		return apartmentNumber;
	}
	public void setApartmentNumber(Integer apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}
	public Integer getRelatedContractNumber() {
		return relatedContractNumber;
	}
	public void setRelatedContractNumber(Integer relatedContractNumber) {
		this.relatedContractNumber = relatedContractNumber;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getBuildingNumber() {
		return buildingNumber;
	}
	public void setBuildingNumber(Integer buildingNumber) {
		this.buildingNumber = buildingNumber;
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
