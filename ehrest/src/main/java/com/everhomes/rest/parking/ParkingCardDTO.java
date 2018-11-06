package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>vendorName: 厂商名称（用于作逻辑，不用于显示），{@link com.everhomes.rest.parking.ParkingLotVendor}</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>cardTypeId: 卡类型id</li>
 * <li>cardType: 卡类型名称</li>
 * <li>cardNumber: 卡号</li>
 * <li>cardName: 卡名称</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 到期时间</li>
 * <li>isValid: isValid</li>
 * <li>freeAmount: 免费金额</li>
 * <li>isSupportOnlinePaid: 是否支持线上支付 {@link ParkingConfigFlag}</li>
 * <li>cardStatus: cardStatus {@link ParkingCardStatus}</li>
 * <li>monthlyDiscount: 月卡优惠折扣</li>
 * </ul>
 */
public class ParkingCardDTO {
	private String ownerType;
	private Long ownerId;
	private Long parkingLotId;
	private String vendorName;
	private String plateNumber;
	private String plateOwnerName;
	private String plateOwnerPhone;
	private String cardTypeId;
	private String cardType;
	private String cardNumber;
	private String cardName;
	private Long startTime;
	private Long endTime;
	@Deprecated
	private Boolean isValid;

	private String freeAmount;
	private Byte isSupportOnlinePaid;

	private Byte cardStatus;

	private String monthlyDiscount;
	public ParkingCardDTO() {

	}

	public Byte getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(Byte cardStatus) {
		this.cardStatus = cardStatus;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPlateOwnerName() {
		return plateOwnerName;
	}

	public void setPlateOwnerName(String plateOwnerName) {
		this.plateOwnerName = plateOwnerName;
	}

	public String getPlateOwnerPhone() {
		return plateOwnerPhone;
	}

	public void setPlateOwnerPhone(String plateOwnerPhone) {
		this.plateOwnerPhone = plateOwnerPhone;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	@Deprecated
	public Boolean getIsValid() {
		return isValid;
	}

	@Deprecated
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getFreeAmount() {
		return freeAmount;
	}

	public void setFreeAmount(String freeAmount) {
		this.freeAmount = freeAmount;
	}

	public Byte getIsSupportOnlinePaid() {
		return isSupportOnlinePaid;
	}

	public void setIsSupportOnlinePaid(Byte isSupportOnlinePaid) {
		this.isSupportOnlinePaid = isSupportOnlinePaid;
	}

	public String getMonthlyDiscount() {
		return monthlyDiscount;
	}

	public void setMonthlyDiscount(String monthlyDiscount) {
		this.monthlyDiscount = monthlyDiscount;
	}

	
}
