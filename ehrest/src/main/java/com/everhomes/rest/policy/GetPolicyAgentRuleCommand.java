package com.everhomes.rest.policy;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id: 主键，不传根据项目id查询</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * </ul>
 */
public class GetPolicyAgentRuleCommand {

    private Long id;
    @NotNull
    private Integer namespaceId;
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
