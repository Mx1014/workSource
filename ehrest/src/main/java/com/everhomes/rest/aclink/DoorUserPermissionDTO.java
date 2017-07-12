package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

public class DoorUserPermissionDTO {
    private Long     id;
    private Byte     status;
    private Byte     authType;
    private String     description;
    private Long     userId;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Long     approveUserId;
    private Long     ownerId;
    private Byte     ownerType;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public Byte getAuthType() {
        return authType;
    }


    public void setAuthType(Byte authType) {
        this.authType = authType;
    }


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


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}