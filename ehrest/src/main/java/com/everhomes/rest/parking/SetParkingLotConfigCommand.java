// @formatter:off
package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>count: 天数</li>
 * </ul>
 */
public class SetParkingLotConfigCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
    private Integer reserveDay;
	private Integer requestNum;
	private Integer rechargeMonthCount;
    private Byte rechargeType;
	
    public SetParkingLotConfigCommand() {
    }
    
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

    public Integer getReserveDay() {
		return reserveDay;
	}

	public void setReserveDay(Integer reserveDay) {
		this.reserveDay = reserveDay;
	}

	public Integer getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(Integer requestNum) {
		this.requestNum = requestNum;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
