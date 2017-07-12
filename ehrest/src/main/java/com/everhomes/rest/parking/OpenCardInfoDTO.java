package com.everhomes.rest.parking;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class OpenCardInfoDTO {

	private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String plateNumber;
    
    private Long openDate;
    private Long expireDate;
    private BigDecimal payMoney;
    
    private String cardNumber;
    private String rateToken;
    private String rateName;
    private BigDecimal monthCount;
    private BigDecimal price;
    private String cardType;
    
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
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public Long getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Long openDate) {
		this.openDate = openDate;
	}
	public Long getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Long expireDate) {
		this.expireDate = expireDate;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
}
