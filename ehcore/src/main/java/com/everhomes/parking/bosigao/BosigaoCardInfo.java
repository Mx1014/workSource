package com.everhomes.parking.bosigao;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class BosigaoCardInfo {

	private String CardID;
	private String PlateNumber;
	private String LimitEnd;

	private BigDecimal Balance;
	private String CardTypeID;
	private String SystemID;
	private String ParkingID;
	private Integer BaseTypeID;
	private Boolean IsAllowOnlIne;
	private BigDecimal Amount;
	private Integer MaxMonth;
	private Integer MaxValue;

	private String ParkName;
	private String VillageID;
	private Integer State;
	private Integer isAdd;
	private String oldCardTypeName;
	private String UserName;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getOldCardTypeName() {
		return oldCardTypeName;
	}

	public void setOldCardTypeName(String oldCardTypeName) {
		this.oldCardTypeName = oldCardTypeName;
	}

	public String getCardID() {
		return CardID;
	}

	public void setCardID(String cardID) {
		CardID = cardID;
	}

	public String getPlateNumber() {
		return PlateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		PlateNumber = plateNumber;
	}

	public String getLimitEnd() {
		return LimitEnd;
	}

	public void setLimitEnd(String limitEnd) {
		LimitEnd = limitEnd;
	}

	public BigDecimal getBalance() {
		return Balance;
	}

	public void setBalance(BigDecimal balance) {
		Balance = balance;
	}

	public String getCardTypeID() {
		return CardTypeID;
	}

	public void setCardTypeID(String cardTypeID) {
		CardTypeID = cardTypeID;
	}

	public String getSystemID() {
		return SystemID;
	}

	public void setSystemID(String systemID) {
		SystemID = systemID;
	}

	public String getParkingID() {
		return ParkingID;
	}

	public void setParkingID(String parkingID) {
		ParkingID = parkingID;
	}

	public Integer getBaseTypeID() {
		return BaseTypeID;
	}

	public void setBaseTypeID(Integer baseTypeID) {
		BaseTypeID = baseTypeID;
	}

	public Boolean getAllowOnlIne() {
		return IsAllowOnlIne;
	}

	public void setAllowOnlIne(Boolean allowOnlIne) {
		IsAllowOnlIne = allowOnlIne;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}

	public Integer getMaxMonth() {
		return MaxMonth;
	}

	public void setMaxMonth(Integer maxMonth) {
		MaxMonth = maxMonth;
	}

	public Integer getMaxValue() {
		return MaxValue;
	}

	public void setMaxValue(Integer maxValue) {
		MaxValue = maxValue;
	}

	public String getParkName() {
		return ParkName;
	}

	public void setParkName(String parkName) {
		ParkName = parkName;
	}

	public String getVillageID() {
		return VillageID;
	}

	public void setVillageID(String villageID) {
		VillageID = villageID;
	}

	public Integer getState() {
		return State;
	}

	public void setState(Integer state) {
		State = state;
	}

	public Integer getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
