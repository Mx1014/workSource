// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 停车场ID</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>name: 停车场名称</li>
 * <li>vendorName: 厂商名称（用于作逻辑，不用于显示），{@link com.everhomes.rest.parking.ParkingLotVendor}</li>
 * <li>createTime: 停车场创建时间</li>
 * </ul>
 */
public class ParkingLotDTO {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String name;
    private String vendorName;
    private Timestamp createTime;
    //是否支持过期充值
    private Byte expiredRechargeFlag;
    //支持过期充值时，最多过期天数
    private Integer maxExpiredDay;
    //支持过期充值时，至少充值几个月
    private Integer expiredRechargeMonthCount;
    //支持过期充值时，按照什么模式充值 {@link ParkingCardExpiredRechargeType}
    private Byte expiredRechargeType;

    //申请月卡的模式
    private Integer flowMode;

    //是否支持临时车缴费
    private Byte tempfeeFlag;
    //是否支持添加/删除费率
    private Byte rateFlag;
    //是否支持锁车
    private Byte lockCarFlag;
    //是否支持寻车
    private Byte searchCarFlag;
    //显示当前在场车/当前剩余车位
    private Byte currentInfoType;
    //停车场客服联系方式
    private String contact;

    public Byte getCurrentInfoType() {
        return currentInfoType;
    }

    public void setCurrentInfoType(Byte currentInfoType) {
        this.currentInfoType = currentInfoType;
    }

    public Byte getSearchCarFlag() {
        return searchCarFlag;
    }

    public void setSearchCarFlag(Byte searchCarFlag) {
        this.searchCarFlag = searchCarFlag;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ParkingLotDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getMaxExpiredDay() {
        return maxExpiredDay;
    }

    public void setMaxExpiredDay(Integer maxExpiredDay) {
        this.maxExpiredDay = maxExpiredDay;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

	public Byte getTempfeeFlag() {
		return tempfeeFlag;
	}

	public void setTempfeeFlag(Byte tempfeeFlag) {
		this.tempfeeFlag = tempfeeFlag;
	}

	public Byte getRateFlag() {
		return rateFlag;
	}

	public void setRateFlag(Byte rateFlag) {
		this.rateFlag = rateFlag;
	}

    public Byte getExpiredRechargeFlag() {
        return expiredRechargeFlag;
    }

    public void setExpiredRechargeFlag(Byte expiredRechargeFlag) {
        this.expiredRechargeFlag = expiredRechargeFlag;
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

    public Integer getFlowMode() {
		return flowMode;
	}

	public void setFlowMode(Integer flowMode) {
		this.flowMode = flowMode;
	}

    public Byte getLockCarFlag() {
        return lockCarFlag;
    }

    public void setLockCarFlag(Byte lockCarFlag) {
        this.lockCarFlag = lockCarFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
