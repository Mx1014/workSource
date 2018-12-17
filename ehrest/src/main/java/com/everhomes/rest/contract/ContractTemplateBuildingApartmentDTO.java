// @formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>buildingName: 楼栋</li>
 * <li>apartmentName: 门牌</li>
 * <li>chargeArea: 收费面积</li>
 * </ul>
 */
public class ContractTemplateBuildingApartmentDTO {
	private String communityName;
	private String communityAddress;
	private String buildingName;
	private String buildingAddress;
	private String apartmentName;
	private Double areaSize;
	private Double chargeArea;
	private Double sharedArea;
	
	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getCommunityAddress() {
		return communityAddress;
	}

	public void setCommunityAddress(String communityAddress) {
		this.communityAddress = communityAddress;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingAddress() {
		return buildingAddress;
	}

	public void setBuildingAddress(String buildingAddress) {
		this.buildingAddress = buildingAddress;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public Double getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}

	public Double getSharedArea() {
		return sharedArea;
	}

	public void setSharedArea(Double sharedArea) {
		this.sharedArea = sharedArea;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
