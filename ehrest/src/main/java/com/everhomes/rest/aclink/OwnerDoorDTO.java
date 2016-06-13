package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class OwnerDoorDTO {
    private Long     ownerId;
    private Byte     ownerType;
    private Long     id;
    private Long     doorId;

    public Long getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Byte getOwnerType() {
        return ownerType;
    }


    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getDoorId() {
        return doorId;
    }


    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

