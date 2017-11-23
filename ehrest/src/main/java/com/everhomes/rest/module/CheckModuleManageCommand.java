package com.everhomes.rest.module;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType: 范围权限</li>
 * <li>ownerId: 范围id</li>
 * <li>moduleId: 模块id</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class CheckModuleManageCommand {

    @NotNull
    private String ownerType;

    @NotNull
    private Long ownerId;

    @NotNull
    private Long organizationId;

    @NotNull
    private Long moduleId;

    @NotNull
    private Long userId;


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

}
