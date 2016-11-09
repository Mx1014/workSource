package com.everhomes.rest.dbsync;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class SyncAppCreateCommand {
    private String     appKey;
    private String     name;
    private String     ownerType;
    private Long     ownerId;
    private Byte     policyType;
    private String     description;
    public String getAppKey() {
        return appKey;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public Byte getPolicyType() {
        return policyType;
    }
    public void setPolicyType(Byte policyType) {
        this.policyType = policyType;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
