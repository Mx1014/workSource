package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class CreateQRUserPermissionCommand {
    private String     description;
    private Long     userId;
    private Integer     namespaceId;
    private Long     approveUserId;
    
    Byte ownerType;
    Long ownerId;
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public Long getApproveUserId() {
        return approveUserId;
    }
    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
