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
    private Double areaSize;
    private String orientation;
    private String communityName;
    private Long communityId;
    private String buildingName;
    
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
	public Double getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
