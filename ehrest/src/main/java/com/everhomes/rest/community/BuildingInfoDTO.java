package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>buildingId: 楼栋id</li>
 *     <li>buildingName: 楼栋名称</li>
 *     <li>areaSize: 建筑面积</li>
 *     <li>rentArea: 在租面积</li>
 *     <li>freeArea: 可招租面积</li>
 *     <li>chargeArea: 收费面积</li>
 *     <li>areaAveragePrice: 在租实时均价（元/平方米）</li>
 *     <li>address: 地址</li>
 *     <li>contactName: 联系人</li>
 *     <li>contactNumber: 联系电话</li>
 * </ul>
 */
public class BuildingInfoDTO {
	
	private Long buildingId;
	private String buildingName;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private Double areaAveragePrice;
	private String address;
	private String contactName;
	private String contactNumber;
	
	
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


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getContactName() {
		return contactName;
	}


	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
