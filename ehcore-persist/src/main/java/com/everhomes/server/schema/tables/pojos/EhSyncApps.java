package com.everhomes.server.schema.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class EhSyncApps implements Serializable {
    private static final long serialVersionUID = 1961941124L;
    private Long id;
    private String appKey;
    private String secretKey;
    private String name;
    private String ownerType;
    private Long ownerId;
    private Byte policyType;
    private Byte state;
    private String description;
    private Timestamp lastUpdate;
    private Timestamp createTime;

    public EhSyncApps() {
    }

    public EhSyncApps(Long id, String appKey, String secretKey, String name, String ownerType, Long ownerId,
            Byte policyType, Byte state, String description, Timestamp lastUpdate, Timestamp createTime) {
        this.id = id;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.name = name;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.policyType = policyType;
        this.state = state;
        this.description = description;
        this.lastUpdate = lastUpdate;
        this.createTime = createTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getPolicyType() {
        return this.policyType;
    }

    public void setPolicyType(Byte policyType) {
        this.policyType = policyType;
    }

    public Byte getState() {
        return this.state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
