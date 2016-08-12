package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>addressId: 地址id</li>
 * <li>buildingName: 楼栋名</li>
 * <li>apartmentName: 门牌号</li>
 * </ul>
 */
public class OwnerAddressDTO {
	
	private Long id;
	
	private Long addressId;
	
	private String buildingName;
	
	private String apartmentName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
