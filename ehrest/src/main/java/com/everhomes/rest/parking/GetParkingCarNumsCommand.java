// @formatter:off
package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>ownerType : 目前是community 参考 {@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId : 目前是小区/园区ID</li>
 * <li>parkingLotId : 停车场id</li>
 * </ul>
 *
 *  @author:dengs 2017年5月27日
 */
public class GetParkingCarNumsCommand {
	@NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long parkingLotId;

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

	public Long getParkingLotId() {
		return parkingLotId;
	}

	public void setParkingLotId(Long parkingLotId) {
		this.parkingLotId = parkingLotId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
