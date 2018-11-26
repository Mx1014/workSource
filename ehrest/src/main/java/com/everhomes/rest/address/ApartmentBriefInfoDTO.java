package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id:房源id</li>
 *  <li>apartmentName:房源名称</li>
 *  <li>livingStatus:房源状态</li>
 * </ul>
 */
public class ApartmentBriefInfoDTO {
	
	private Long id;
	private String apartmentName;
	private Byte livingStatus;
	
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
	public Byte getLivingStatus() {
		return livingStatus;
	}
	public void setLivingStatus(Byte livingStatus) {
		this.livingStatus = livingStatus;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
