package com.everhomes.rest.organization.pm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;
/**
<ul>
<li>id: 房源id</li>
<li>apartmentName: 房源名称</li>
<li>communityId: 园区id</li>
<li>communityName: 园区名称</li>
<li>buildingId: 所在楼宇id</li>
<li>buildingName: 所在楼宇名称</li>
<li>apartmentFloor: 房源所在楼层</li>
<li>livingStatus: 房源状态</li>
<li>areaSize: 建筑面积</li>
<li>rentArea: 在租面积</li>
<li>freeArea: 可招租面积</li>
<li>chargeArea: 收费面积</li>
</ul>
*/
public class ApartmentForAPPDTO implements Comparable<ApartmentForAPPDTO>{
	
	private Long id;
	private String apartmentName;
	private Long communityId;
	private String communityName;
	private Long buildingId;
	private String buildingName;
	private String apartmentFloor;
	private Byte livingStatus;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	
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
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
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
	public String getApartmentFloor() {
		return apartmentFloor;
	}
	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
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
	
	@Override
    public boolean equals(Object obj){
        if (! (obj instanceof ApartmentForAPPDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

	@Override
	public int compareTo(ApartmentForAPPDTO o) {
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
