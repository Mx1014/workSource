package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 目前是community 参考 {@link ParkingOwnerType}</li>
 * <li>ownerId: 目前是小区/园区ID</li>
 * <li>parkingLotId: 停车场id</li>
 * <li>pageAnchor: id</li>
 * <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListParkingCarVerificationsCommand {

    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;

    private Long pageAnchor;
    private Integer pageSize;

    private Long currentPMId;
    private Long currentProjectId;
    private Long appId;

    public Long getCurrentPMId() {
        return currentPMId;
    }

    public void setCurrentPMId(Long currentPMId) {
        this.currentPMId = currentPMId;
    }

    public Long getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(Long currentProjectId) {
        this.currentProjectId = currentProjectId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
