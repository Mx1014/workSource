package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class UserImpersonationDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;
    private String description;
    private Byte status;
    
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
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
    public String getTargetType() {
        return targetType;
    }
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
