package com.everhomes.yellowPage;


import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

public class ServiceAllianceRequestInfo {

    private Long id;
    private String templateType;
    private String ownerType;
    private Long ownerId;
    private Long type;
    private String creatorName;
    private String creatorMobile;
    private Long creatorOrganizationId;
    private Long serviceAllianceId;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
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

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorMobile() {
        return creatorMobile;
    }

    public void setCreatorMobile(String creatorMobile) {
        this.creatorMobile = creatorMobile;
    }

    public Long getCreatorOrganizationId() {
        return creatorOrganizationId;
    }

    public void setCreatorOrganizationId(Long creatorOrganizationId) {
        this.creatorOrganizationId = creatorOrganizationId;
    }

    public Long getServiceAllianceId() {
        return serviceAllianceId;
    }

    public void setServiceAllianceId(Long serviceAllianceId) {
        this.serviceAllianceId = serviceAllianceId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
