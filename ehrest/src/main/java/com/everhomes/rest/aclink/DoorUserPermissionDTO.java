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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}