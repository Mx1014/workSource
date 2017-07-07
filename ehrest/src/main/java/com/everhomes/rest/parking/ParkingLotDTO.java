// @formatter:off
package com.everhomes.rest.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 停车场ID</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>name: 停车场名称</li>
 * <li>vendorName: 厂商名称（用于作逻辑，不用于显示），{@link com.everhomes.rest.parking.ParkingLotVendor}</li>
 * <li>cardReserveDays: 在该停车场所发的月卡，保留多少天等待申请者来领取</li>
 * <li>createTime: 停车场创建时间</li>
 * </ul>
 */
public class ParkingLotDTO {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String name;
    private String vendorName;
    private Integer cardReserveDays;
    private Timestamp createTime;
    
    private Byte tempfeeFlag;
    private Byte rateFlag;
    
    private Byte isSupportRecharge;
    private Integer rechargeMonthCount;
    private Byte rechargeType;
    
    private Integer flowMode;

    private Byte lockCarFlag;
    private String contact;

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

    public Integer getCardReserveDays() {
        return cardReserveDays;
    }

    public void setCardReserveDays(Integer cardReserveDays) {
        this.cardReserveDays = cardReserveDays;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getIsSupportRecharge() {
		return isSupportRecharge;
	}

	public void setIsSupportRecharge(Byte isSupportRecharge) {
		this.isSupportRecharge = isSupportRecharge;
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
}
