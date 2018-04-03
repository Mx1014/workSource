package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>materialId: 物品id</li>
 *     <li>communityId: 园区id</li>
 *     <li>ownerType: 物品所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 物品所属类型id</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class DeleteWarehouseMaterialCommand {

    private String ownerType;

    private Long ownerId;

    private Long materialId;

    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
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
