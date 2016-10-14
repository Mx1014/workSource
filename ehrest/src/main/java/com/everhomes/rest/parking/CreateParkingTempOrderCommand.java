package com.everhomes.rest.parking;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class CreateParkingTempOrderCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
    private String plateNumber;
	@NotNull
    private Long payerEnterpriseId;
	@NotNull
    private String orderToken;
	@NotNull
    private BigDecimal price;
	
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
	public Long getPayerEnterpriseId() {
		return payerEnterpriseId;
	}
	public void setPayerEnterpriseId(Long payerEnterpriseId) {
		this.payerEnterpriseId = payerEnterpriseId;
	}
	public String getOrderToken() {
		return orderToken;
	}
	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
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
}
