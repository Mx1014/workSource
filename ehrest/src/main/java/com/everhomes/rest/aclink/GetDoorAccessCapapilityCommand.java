package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 获取门禁能力
 * <li>ownerType: 参考{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>ownerId: 属于的对象 ID</li>
 * <li>namespaceId: 域空间 ID</li>
 * </ul>
 * @author janson
 *
 */
public class GetDoorAccessCapapilityCommand {
    @NotNull
    private Integer namespaceId;
    
    @NotNull
    private Byte ownerType;
    
    @NotNull
    private Long ownerId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
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
