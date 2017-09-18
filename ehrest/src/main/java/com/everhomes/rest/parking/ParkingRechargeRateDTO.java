// @formatter:off
package com.everhomes.rest.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>areaId: 停车场  区域ID 博思高没有区域，ETCP有的停车场有区域</li>
 * <li>vendorName: com.everhomes.rest.parking.ParkingLotVendor: 停车场厂商</li>
 * <li>rateToken: 费率ID，不同厂商有不同类型的ID</li>
 * <li>rateName: 费率名称</li>
 * <li>monthCount: 多少个月，半个月用0.5表示，博思高厂商有值，ETCP厂商无值</li>
 * <li>price: 价格</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class ParkingRechargeRateDTO {
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private Long areaId;
    private String vendorName;
    private String rateToken;
    private String rateName;
    private BigDecimal monthCount;
    private BigDecimal price;
    private Timestamp createTime;
    private String cardType;
    private BigDecimal originalPrice;

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public ParkingRechargeRateDTO() {
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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRateToken() {
        return rateToken;
    }

    public void setRateToken(String rateToken) {
        this.rateToken = rateToken;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public BigDecimal getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(BigDecimal monthCount) {
        this.monthCount = monthCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}
