// @formatter:off
package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>count: 发放卡的张数</li>
 * </ul>
 */
public class IssueParkingCardsCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
    private Integer count;
	
	private Long flowId;
	
	private Byte status;
	
    public IssueParkingCardsCommand() {
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
