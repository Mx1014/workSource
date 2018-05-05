package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerId: 项目ID</li>
 * <li>ownerType: 项目类型</li>
 * </ul>
 *
 */

public class ListDoorAccessByUserCommand {

    @NotNull
    private Integer namespaceId;

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

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
}
