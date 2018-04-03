package com.everhomes.rest.community_approve;

/**
 * <ul>
 * <li>ownerType: 目前默认是： EhOrganizations</li>
 * <li>ownerId: 目前是 organizationId </li>
 * <li>moduleId: 模块id</li>
 * <li>moduleType: 模块类型,模块类型 默认"any-module",参考 {@link com.everhomes.rest.flow.FlowModuleType} </li>
 * </ul>
 *
 */
public class ListCommunityApproveCommand {
    private Long ownerId;
    private String ownerType;
    private Long moduleId;
    private String moduleType;

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

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}
