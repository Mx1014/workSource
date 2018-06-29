package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>spaceNo: 车位编号</li>
 * <li>spaceAddress: 车位地址</li>
 * <li>lockId: 锁id</li>
 * <li>parkingHubsId: hub的id，searchParkingHubs接口中返回的id</li>
 * <li>status: 是否开放 1：开放，0：关闭 {@link com.everhomes.rest.parking.ParkingSpaceStatus}</li>
 * </ul>
 */
public class AddParkingSpaceCommand {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String spaceNo;
    private String spaceAddress;
    private String lockId;
    private Long parkingHubsId;
    private Byte status;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    public String getSpaceAddress() {
        return spaceAddress;
    }

    public void setSpaceAddress(String spaceAddress) {
        this.spaceAddress = spaceAddress;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public Long getParkingHubsId() {
        return parkingHubsId;
    }

    public void setParkingHubsId(Long parkingHubsId) {
        this.parkingHubsId = parkingHubsId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
