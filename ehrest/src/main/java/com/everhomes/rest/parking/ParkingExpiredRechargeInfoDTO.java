package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>rateToken: 费率ID</li>
 * <li>rateName: 费率名称</li>
 * <li>monthCount: 充值月数</li>
 * <li>originalPrice: 原价</li>
 * <li>price: 实际价格</li>
 * <li>cardTypeId: 卡类型id</li>
 * <li>cardTypeName: 卡类型名称</li>
 * <li>startPeriod: 充值开始时间</li>
 * <li>endPeriod: 充值结束时间</li>
 * <li>entryTime: 过期入场时间</li>
 * </ul>
 */
public class ParkingExpiredRechargeInfoDTO {

    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String rateToken;
    private String rateName;
    private BigDecimal monthCount;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private String cardTypeId;
    private String cardTypeName;
    private Long startPeriod;
    private Long endPeriod;
    private Long entryTime;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Long startPeriod) {
        this.startPeriod = startPeriod;
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

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public Long getEndPeriod() {
        return endPeriod;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Long entryTime) {
        this.entryTime = entryTime;
    }

    public void setEndPeriod(Long endPeriod) {
        this.endPeriod = endPeriod;
    }
}
