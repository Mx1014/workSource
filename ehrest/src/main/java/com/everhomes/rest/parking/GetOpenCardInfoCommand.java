package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>parkingRequestId: 申请月卡记录ID</li>
 * </ul>
 */
public class GetOpenCardInfoCommand {

	private String ownerType;
	private Long ownerId;
	private Long parkingLotId;
	private String plateNumber;
	private Long parkingRequestId;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getParkingRequestId() {
		return parkingRequestId;
	}

	public void setParkingRequestId(Long parkingRequestId) {
		this.parkingRequestId = parkingRequestId;
	}
}
