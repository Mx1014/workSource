package com.everhomes.rest.parking;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>openDate: 开卡日起</li>
 * <li>expireDate: 开卡成功后有效期</li>
 * <li>payMoney: 开卡需要支付的金额</li>
 * <li>cardNumber: 卡号</li>
 * <li>rateToken: 费率ID</li>
 * <li>rateName: 费率名称</li>
 * <li>monthCount: 开卡月数</li>
 * <li>price: 费率价格</li>
 * <li>cardType: 卡类型</li>
 * <li>orderType: 订单类型(主要用来区分是充值还是开卡) 1:月卡缴费或者临时缴费订单, 2:开卡支付订单{@link ParkingOrderType}</li>
 * </ul>
 */
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
	private Byte orderType;

	public Byte getOrderType() {
		return orderType;
	}

	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
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
