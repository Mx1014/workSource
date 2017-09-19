// @formatter:off
package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>expiredRechargeFlag: 是否支持过期充值</li>
 * <li>maxExpiredDay: 最多过期天数</li>
 * <li>expiredRechargeMonthCount: 过期续交时，预交月数</li>
 * <li>expiredRechargeType: 预交，计费方式 1: 整月，2:实际天数   {@link ParkingCardExpiredRechargeType}</li>
 * </ul>
 */
public class SetParkingLotConfigCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;

	//是否支持过期充值
	private Byte expiredRechargeFlag;
	//支持过期充值时，最多过期天数
	private Integer maxExpiredDay;
	//支持过期充值时，至少充值几个月
	private Integer expiredRechargeMonthCount;
	//支持过期充值时，按照什么模式充值 {@link ParkingCardExpiredRechargeType}
	private Byte expiredRechargeType;

	private Byte monthlyDiscountFlag;
	private Integer monthlyDiscount;

	private Byte tempFeeDiscountFlag;
	private Integer tempFeeDiscount;

	public Byte getMonthlyDiscountFlag() {
		return monthlyDiscountFlag;
	}

	public void setMonthlyDiscountFlag(Byte monthlyDiscountFlag) {
		this.monthlyDiscountFlag = monthlyDiscountFlag;
	}

	public Integer getMonthlyDiscount() {
		return monthlyDiscount;
	}

	public void setMonthlyDiscount(Integer monthlyDiscount) {
		this.monthlyDiscount = monthlyDiscount;
	}

	public Byte getTempFeeDiscountFlag() {
		return tempFeeDiscountFlag;
	}

	public void setTempFeeDiscountFlag(Byte tempFeeDiscountFlag) {
		this.tempFeeDiscountFlag = tempFeeDiscountFlag;
	}

	public Integer getTempFeeDiscount() {
		return tempFeeDiscount;
	}

	public void setTempFeeDiscount(Integer tempFeeDiscount) {
		this.tempFeeDiscount = tempFeeDiscount;
	}

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

	public Byte getExpiredRechargeFlag() {
		return expiredRechargeFlag;
	}

	public void setExpiredRechargeFlag(Byte expiredRechargeFlag) {
		this.expiredRechargeFlag = expiredRechargeFlag;
	}

	public Integer getMaxExpiredDay() {
		return maxExpiredDay;
	}

	public void setMaxExpiredDay(Integer maxExpiredDay) {
		this.maxExpiredDay = maxExpiredDay;
	}

	public Integer getExpiredRechargeMonthCount() {
		return expiredRechargeMonthCount;
	}

	public void setExpiredRechargeMonthCount(Integer expiredRechargeMonthCount) {
		this.expiredRechargeMonthCount = expiredRechargeMonthCount;
	}

	public Byte getExpiredRechargeType() {
		return expiredRechargeType;
	}

	public void setExpiredRechargeType(Byte expiredRechargeType) {
		this.expiredRechargeType = expiredRechargeType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
