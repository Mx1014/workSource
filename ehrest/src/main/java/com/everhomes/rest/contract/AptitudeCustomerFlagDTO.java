package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class AptitudeCustomerFlagDTO {

    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long id;
    private Byte value;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
