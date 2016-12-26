package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class GetParkingRequestCardConfigCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
	private Long flowId;

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

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
