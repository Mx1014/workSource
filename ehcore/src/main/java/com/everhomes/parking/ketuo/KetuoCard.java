package com.everhomes.parking.ketuo;

/**
 * <ul>
 * <li>cardId: 充值车ID 续费充值时使用</li>
 * <li>plateNo: 车牌号</li>
 * <li>carType: 车类型 2:月租</li>
 * <li>validFrom: 有效开始时间 yyyy-MM-dd HH:mm:ss</li>
 * <li>validTo: 有效结束时间 yyyy-MM-dd HH:mm:ss</li>
 * <li>freeMoney: 免费金额(单位分) 注意：每个月的免费金额</li>
 * <li>isAllow: 0:不允许 1：允许</li>
 * <li>name: 车主姓名</li>
 * </ul>
 */
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
