// @formatter:off
package com.everhomes.rest.parking;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>rateName: 费率名称</li>
 * <li>cardType: cardType</li>
 * <li>monthCount: 月数</li>
 * <li>price: 总价格</li>
 * </ul>
 */
public class CreateParkingRechargeRateCommand {
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long parkingLotId;
    private String rateName;
    @NotNull
    private String cardTypeName;
    @NotNull
    private BigDecimal monthCount;
    @NotNull
    private BigDecimal price;
    private String cardTypeId;

    
    public String getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public CreateParkingRechargeRateCommand() {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

}
