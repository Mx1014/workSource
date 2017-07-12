package com.everhomes.parking.ketuo;

public class KetuoCard {
	private Integer cardId;
	private String plateNo;
	private String carType;
	private String validFrom;
	private String validTo;
	private Integer freeMoney;
	private Integer isAllow;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCardId() {
		return cardId;
	}
	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public Integer getFreeMoney() {
		return freeMoney;
	}
	public void setFreeMoney(Integer freeMoney) {
		this.freeMoney = freeMoney;
	}
	public Integer getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(Integer isAllow) {
		this.isAllow = isAllow;
	}
	
	
}
