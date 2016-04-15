// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 活动ID</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>startTime: 活动开始时间</li>
 * <li>endTime: 活动结束时间</li>
 * <li>topCount: 前多少位可以参加活动</li>
 * </ul>
 */
public class SetParkingActivityCommand {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String startTime;
    private String endTime;
    private Integer topCount;
    
    public SetParkingActivityCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public Integer getTopCount() {
        return topCount;
    }

    public void setTopCount(Integer topCount) {
        this.topCount = topCount;
    }
    
    
    public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
