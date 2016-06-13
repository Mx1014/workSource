package com.everhomes.rest.techpark.park;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;

public class PlateInfo {
	
	private String plateNumber;
	
	private String ownerName;
	
	private Timestamp validityPeriod;
	
	private String cardType;
	
	private String cardCode;
	
	private String isValid;
	
	@ItemType(value = ParkingChargeDTO.class)
	private List<ParkingChargeDTO> parkingCharge;
	
	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Timestamp getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Timestamp validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public List<ParkingChargeDTO> getParkingCharge() {
		return parkingCharge;
	}

	public void setParkingCharge(List<ParkingChargeDTO> parkingCharge) {
		this.parkingCharge = parkingCharge;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

}
