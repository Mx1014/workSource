package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType : 目前是community 参考 {@link ParkingOwnerType}</li>
 * <li>ownerId : 目前是小区/园区ID</li>
 * <li>parkingLotId : 停车场id</li>
 * <li>freeSpaceNum : 空余车位</li>
 * </ul>
 */
public class ParkingFreeSpaceNumDTO {
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private Integer freeSpaceNum;

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

    public Integer getFreeSpaceNum() {
        return freeSpaceNum;
    }

    public void setFreeSpaceNum(Integer freeSpaceNum) {
        this.freeSpaceNum = freeSpaceNum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
