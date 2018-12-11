package com.everhomes.rest.address;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id:房源id</li>
 *  <li>apartmentName:房源名称</li>
 *  <li>livingStatus:房源状态</li>
 * </ul>
 */
public class ApartmentBriefInfoDTO implements Comparable<ApartmentBriefInfoDTO>{
	
	private Long id;
	private String apartmentName;
	private Byte livingStatus;
    private Double areaSize;
    private String orientation;
    private String communityName;
    private Long communityId;
    private String buildingName;
    private String apartmentFloor;
    
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
	public String getApartmentFloor() {
		return apartmentFloor;
	}
	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	@Override
    public boolean equals(Object obj){
        if (! (obj instanceof ApartmentBriefInfoDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

	@Override
	public int compareTo(ApartmentBriefInfoDTO o) {
		int f1 = parseInt(getApartmentFloor());
		int f2 = parseInt(o.getApartmentFloor());
		if (f1 == f2) {
			return getApartmentName().compareTo(o.getApartmentName());
		}
		return f1 - f2;
	}
	
	private int parseInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			return 0;
		}
	}
}
