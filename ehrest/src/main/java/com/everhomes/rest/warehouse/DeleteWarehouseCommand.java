package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>warehouseId: 仓库id</li>
 *     <li>ownerType: 仓库所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 仓库所属类型id</li>
 *     <li>communityId: 园区id</li>
 * </ul>
 * Created by ying.xiong on 2017/5/10.
 */
public class DeleteWarehouseCommand {

    private String ownerType;

    private Long ownerId;

    private Long communityId;

    private Long warehouseId;

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

    public Long getWarehouseId() {
        return warehouseId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
