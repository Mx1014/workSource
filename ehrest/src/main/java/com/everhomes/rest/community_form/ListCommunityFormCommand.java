package com.everhomes.rest.community_form;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType: 范围权限</li>
 * <li>ownerId: 范围id</li>
 * <li>moduleId: 模块id</li>
 * <li>appId: 应用id</li>
 * <li>userId: 用户id</li>
 * <li>organizationId: 企业id</li>
 * <li>communityFetchType：查询类型</li>
 * <li>type:表单相关类型</li>
 * </ul>
 */
public class ListCommunityFormCommand {

    @NotNull
    private String ownerType;

    @NotNull
    private Long ownerId;

    @NotNull
    private Long organizationId;

    @NotNull
    private Long moduleId;

    @NotNull
    private Long appId;

    @NotNull
    private Long userId;

    private String communityFetchType;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getCommunityFetchType() {
        return communityFetchType;
    }

    public void setCommunityFetchType(String communityFetchType) {
        this.communityFetchType = communityFetchType;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
