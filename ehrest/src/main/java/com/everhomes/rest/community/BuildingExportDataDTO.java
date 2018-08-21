package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>name:楼栋名</li>
 *  <li>aliasName:楼栋别名</li>
 *  <li>floorNumber:楼层数</li>
 *  <li>areaSize:建筑面积</li>
 *  <li>rentArea:在租面积</li>
 *  <li>freeArea:可招租面积</li>
 *  <li>chargeArea:收费面积</li>
 *  <li>address:地址</li>
 *  <li>latitudeLongitude:经纬度</li>
 *  <li>managerName:联系人</li>
 *  <li>contact:联系电话</li>
 *  <li>description:楼栋介绍</li>
 *  <li>trafficDescription:交通说明</li>
 * </ul>
 */
public class BuildingExportDataDTO {
	
	private String name;
    private String buildingNumber;
    private String aliasName;
    private Integer floorNumber;
    private Double areaSize;
    private Double rentArea;
    private Double freeArea;
    private Double chargeArea;
    private String address;
    private String latitudeLongitude;
    private String managerName;
    private String contact;
    private String description;
    private String trafficDescription;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBuildingNumber() {
		return buildingNumber;
	}
	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public Integer getFloorNumber() {
		return floorNumber;
	}
	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}
	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTrafficDescription() {
		return trafficDescription;
	}
	public void setTrafficDescription(String trafficDescription) {
		this.trafficDescription = trafficDescription;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
