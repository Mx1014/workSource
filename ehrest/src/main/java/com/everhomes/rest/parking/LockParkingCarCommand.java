package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2017/4/10.
 */
public class LockParkingCarCommand {
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long parkingLotId;
    @NotNull
    private String plateNumber;

    private Byte lockStatus;

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

    public Byte getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Byte lockStatus) {
        this.lockStatus = lockStatus;
    }
}
