package com.everhomes.rest.parking;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class ParkingTempFeeDTO {
	private String plateNumber;
	private Long entryTime;
	private Long payTime;
	private Integer parkingTime;
	private BigDecimal price;
	private Integer delayTime;
	private String orderToken;
	
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
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
