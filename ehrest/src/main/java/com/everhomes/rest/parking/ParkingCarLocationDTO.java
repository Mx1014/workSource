package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType : 目前是community 参考 {@link ParkingOwnerType}</li>
 * <li>ownerId : 目前是小区/园区ID</li>
 * <li>parkingLotId : 停车场id</li>
 * <li>plateNumber : 车牌</li>
 * <li>spaceNo: 车位编号</li>
 * <li>parkingName: 停车场名称</li>
 * <li>floorName: 楼层</li>
 * <li>location: 车位位置</li>
 * <li>parkingTime: 停车时间</li>
 * <li>entryTime: 入场时间</li>
 * <li>carImageUrl: 车辆图片url</li>
 * </ul>
 */
public class ParkingCarLocationDTO {
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String plateNumber;

    private String spaceNo;
    private String parkingName;
    private String floorName;
    private String location;
    private String parkingTime;
    private Long entryTime;
    private String carImageUrl;

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

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(String parkingTime) {
        this.parkingTime = parkingTime;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Long entryTime) {
        this.entryTime = entryTime;
    }

    public String getCarImageUrl() {
        return carImageUrl;
    }

    public void setCarImageUrl(String carImageUrl) {
        this.carImageUrl = carImageUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
