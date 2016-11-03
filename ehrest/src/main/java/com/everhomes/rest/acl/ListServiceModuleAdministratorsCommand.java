package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.util.StringHelper;
import com.sun.istack.internal.NotNull;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>moduleId: 业务模块id</li>
 * </ul>
 */
public class ListServiceModuleAdministratorsCommand {

    @NotNull
    private Long organizationId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long ownerId;

    @NotNull
    private Long moduleId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
