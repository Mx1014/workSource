package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 场景id</li>
 * <li>ownerType: 场景类型</li>
 * </ul>
 */
public class GetUserRealNameCommand {

    private Long ownerId;

    private String ownerType;

    public GetUserRealNameCommand() {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
