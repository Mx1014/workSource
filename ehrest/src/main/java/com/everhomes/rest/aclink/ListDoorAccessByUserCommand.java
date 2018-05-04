package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>namespace: 域空间</li>
 * <li>ownerId: 项目ID</li>
 * <li>ownerType: 项目类型</li>
 * </ul>
 *
 */

public class ListDoorAccessByUserCommand {

    @NotNull
    private Long namespace;

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    public Long getNamespace() {
        return namespace;
    }

    public void setNamespace(Long namespace) {
        this.namespace = namespace;
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
