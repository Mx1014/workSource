// @formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>buildingName: 楼栋</li>
 * <li>apartmentName: 门牌</li>
 * <li>addressId: 门牌id</li>
 * <li>areaSize: 面积</li>
 * </ul>
 */
public class BuildingApartmentDTO {
	private Long id;
	private Long addressId;
	private String buildingName;
	private String apartmentName;
	private Double areaSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

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
