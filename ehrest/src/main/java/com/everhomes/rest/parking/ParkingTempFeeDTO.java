package com.everhomes.rest.parking;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>plateNumber: 车牌</li>
 * <li>entryTime: 入场时间</li>
 * <li>payTime: 查询时间，即计算临时费用时间</li>
 * <li>parkingTime: 停车时间 单位分钟</li>
 * <li>price: 金额</li>
 * <li>delayTime: 剩余免费总时间</li>
 * <li>orderToken: 订单token</li>
 * <li>originalPrice: 原价</li>
 * <li>remainingTime: 剩余免费时间</li>
<<<<<<< HEAD
 * <li>tempFeeDiscount: 临时车优惠折扣</li>
=======
 * <li>temfeeDiscount：临时车优惠折扣</li>
>>>>>>> 5.10.0
 * </ul>
 */
public class ParkingTempFeeDTO {
	private String plateNumber;
	private Long entryTime;
	private Long payTime;
	private Integer parkingTime;
	private BigDecimal price;
	private Integer delayTime;
	private String orderToken;
	private BigDecimal originalPrice;
	private Integer remainingTime;
	private String tempFeeDiscount;

	public Integer getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(Integer remainingTime) {
		this.remainingTime = remainingTime;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Long getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Long entryTime) {
		this.entryTime = entryTime;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public Integer getParkingTime() {
		return parkingTime;
	}

	public void setParkingTime(Integer parkingTime) {
		this.parkingTime = parkingTime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	public String getOrderToken() {
		return orderToken;
	}

	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
	}

	
	public String getTempFeeDiscount() {
		return tempFeeDiscount;
	}

	public void setTempFeeDiscount(String tempFeeDiscount) {
		this.tempFeeDiscount = tempFeeDiscount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
