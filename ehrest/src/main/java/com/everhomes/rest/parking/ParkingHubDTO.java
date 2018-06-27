package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>hubName: hub名称</li>
 * <li>hubMac: hub的硬件编码</li>
 * <li>relatedSpaceNos: 关联的车位列表</li>
 * </ul>
 */
public class ParkingHubDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String hubName;
    private String hubMac;
    private List<String> relatedSpaceNos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public String gethubMac() {
        return hubMac;
    }

    public void sethubMac(String hubMac) {
        this.hubMac = hubMac;
    }

    public List<String> getRelatedSpaceNos() {
        return relatedSpaceNos;
    }

    public void setRelatedSpaceNos(List<String> relatedSpaceNos) {
        this.relatedSpaceNos = relatedSpaceNos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
