package com.everhomes.rest.asset.modulemapping;


/**
 * @author created by ycx
 * @date 下午1:38:48
 */

public class ParkingInstanceConfigDTO {
    private Long parkingOriginId;
    private String ownerType;
    private Long ownerId;
    private Long billGroupId;
    private Long chargingItemId;
    
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getBillGroupId() {
		return billGroupId;
	}
	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}
	public Long getChargingItemId() {
		return chargingItemId;
	}
	public void setChargingItemId(Long chargingItemId) {
		this.chargingItemId = chargingItemId;
	}
	public Long getParkingOriginId() {
		return parkingOriginId;
	}
	public void setParkingOriginId(Long parkingOriginId) {
		this.parkingOriginId = parkingOriginId;
	}
}
