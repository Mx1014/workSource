package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *
 * <ul>
 * <li>ownerType : 目前是community 参考 {@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId : 目前是小区/园区ID</li>
 * <li>parkingLotId : 停车场id</li>
 * <li>plateNumber : 车牌</li>
 * <li>orderId : 订单id</li>
 * </ul>
 *
 */
public class UpdateParkingOrderCommand {
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long parkingLotId;
    @NotNull
    private String plateNumber;

    private Long orderId;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
