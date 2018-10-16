package com.everhomes.rest.investmentAd;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>buildingId：楼宇id</li>
 * 	<li>buildingName：楼宇名称 </li>
 *	<li>apartmentId：房源id</li>
 * 	<li>apartmentName：房源名称</li>
 * </ul>
 */
public class RelatedAssetDTO {
	
	private Long buildingId;
	private String buildingName;
	private Long apartmentId;
	private String apartmentName;
	
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

	public Long getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
