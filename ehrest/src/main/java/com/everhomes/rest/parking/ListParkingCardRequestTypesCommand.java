package com.everhomes.rest.parking;

/**
 * @author sw on 2017/9/18.
 */
public class ListParkingCardRequestTypesCommand {

    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;

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

}
