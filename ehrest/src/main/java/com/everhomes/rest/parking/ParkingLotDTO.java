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
    private Integer maxRequestNum;
    private Byte tempfeeFlag;
    private Byte rateFlag;
    
    private Integer monthCount;
    private Byte rechargeType;
    private String cardRequestTip;
    private String cardAgreement;
    private Integer maxIssueNum;
    
    private BigDecimal totalAmount;
    
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Integer getMaxRequestNum() {
		return maxRequestNum;
	}

	public void setMaxRequestNum(Integer maxRequestNum) {
		this.maxRequestNum = maxRequestNum;
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
}
