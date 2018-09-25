package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id:房源id</li>
 *  <li>apartmentName:房源名称</li>
 * </ul>
 */
public class ApartmentBriefInfoDTO {
	
	private Long id;
	private String apartmentName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
