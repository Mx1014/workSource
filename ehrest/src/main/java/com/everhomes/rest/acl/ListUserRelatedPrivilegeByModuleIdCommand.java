package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType: 类型 EhCommunities, EhOrganizations，参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId: 范围id</li>
 * <li>organizationId: 机构id</li>
 * <li>moduleId: 模块id</li>
 * <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class ListUserRelatedPrivilegeByModuleIdCommand {

    private Integer namespaceId;

    private String ownerType;

    private Long ownerId;

    private Long organizationId;

    @NotNull
    private Long moduleId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
