package com.everhomes.rest.dbsync;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class SyncAppDTO {
    private String     appKey;
    private Timestamp     lastUpdate;
    private String     name;
    private String     ownerType;
    private Timestamp     createTime;
    private String     secretKey;
    private Byte     state;
    private Byte     policyType;
    private Long     ownerId;
    private Long     id;
    private String     description;


    public String getAppKey() {
        return appKey;
    }


    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }


    public Timestamp getLastUpdate() {
        return lastUpdate;
    }


    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
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


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public String getSecretKey() {
        return secretKey;
    }


    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public Byte getState() {
        return state;
    }


    public void setState(Byte state) {
        this.state = state;
    }


    public Byte getPolicyType() {
        return policyType;
    }


    public void setPolicyType(Byte policyType) {
        this.policyType = policyType;
    }


    public Long getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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
