package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>spaceAddress: 车位地址</li>
 * <li>lockId: 锁id</li>
 * <li>parkingHubsId: hub的id，searchParkingHubs接口中返回的id</li>
 * </ul>
 */
public class UpdateParkingSpaceCommand {
    private Long id;
    private String spaceAddress;
    private String lockId;
    private Long parkingHubsId;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
