// @formatter:off
package com.everhomes.rest.family;


/**
 * <ul>
 * <li>longitude: 邻居用户当前经度</li>
 * <li>userName: 邻居用户当前纬度</li>
 * <li>userName: 邻居用户地址的楼栋号</li>
 * <li>apartmentFloor: 邻居用户地址的楼层号</li>
 * <li>initial: 首字母</li>
 * </ul>
 */
public class NeighborUserDetailDTO extends NeighborUserDTO{
    private Long longitude;
    private Long latitude;
    private String buildingName;
    private String apartmentFloor;
    private String initial;

    public NeighborUserDetailDTO() {
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
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

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}
    
}
