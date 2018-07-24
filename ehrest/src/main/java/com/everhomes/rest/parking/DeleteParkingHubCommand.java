package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型，{@link ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * </ul>
 */
public class DeleteParkingHubCommand {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

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
}
