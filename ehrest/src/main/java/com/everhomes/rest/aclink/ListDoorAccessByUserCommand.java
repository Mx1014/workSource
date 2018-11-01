package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerId: 项目ID</li>
 * <li>ownerType: 项目类型</li>
 * <li>doorType: 门禁类型 (14:旺龙梯控组 16：旺龙门禁组 null：旺龙门禁组+梯控组)</li>
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

    private Byte doorType;

    public Byte getDoorType() {
        return doorType;
    }

    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }

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
