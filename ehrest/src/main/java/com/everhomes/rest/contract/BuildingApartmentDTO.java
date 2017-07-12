// @formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>buildingName: 楼栋</li>
 * <li>apartmentName: 门牌</li>
 * </ul>
 */
public class BuildingApartmentDTO {
	private String buildingName;
	private String apartmentName;
	
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
