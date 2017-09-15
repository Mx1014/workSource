// @formatter:off
package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>isSupportRecharge: 是否支持过期充值</li>
 * <li>reserveDay: 最多过期天数</li>
 * <li>rechargeMonthCount: 过期续交时，预交月数</li>
 * <li>rechargeType: 预交，计费方式 1: 整月，2:实际天数   {@link ParkingCardExpiredRechargeType}</li>
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
	private Byte isSupportRecharge;
    private Integer reserveDay;
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

	public Integer getRechargeMonthCount() {
		return rechargeMonthCount;
	}

	public void setRechargeMonthCount(Integer rechargeMonthCount) {
		this.rechargeMonthCount = rechargeMonthCount;
	}

	public Byte getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(Byte rechargeType) {
		this.rechargeType = rechargeType;
	}

	public Byte getIsSupportRecharge() {
		return isSupportRecharge;
	}

	public void setIsSupportRecharge(Byte isSupportRecharge) {
		this.isSupportRecharge = isSupportRecharge;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
