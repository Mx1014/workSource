package com.everhomes.rest.module.security;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: ownerType对应的对象ID</li>
 * <li>ownerType: 对象类型</li>
 * <li>moduleId: 应用ID</li>
 * </ul>
 */
public class GetServiceModuleSecurityStatusCommand {
    private Long ownerId;
    private String ownerType;
    private Long moduleId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
