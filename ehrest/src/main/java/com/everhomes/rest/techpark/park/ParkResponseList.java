package com.everhomes.rest.techpark.park;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ParkResponseList {

	@ItemType(value = ParkingChargeDTO.class)
	private List<ParkingChargeDTO> parkingCharge;
	
	private Integer nextPageOffset;

	public List<ParkingChargeDTO> getParkingCharge() {
		return parkingCharge;
	}

	public void setParkingCharge(List<ParkingChargeDTO> parkingCharge) {
		this.parkingCharge = parkingCharge;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

}
