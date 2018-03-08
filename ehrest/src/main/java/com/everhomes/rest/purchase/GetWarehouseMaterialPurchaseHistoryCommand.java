//@formatter:off
package com.everhomes.rest.purchase;

/**
 * Created by Wentian Wang on 2018/3/8.
 */
/**
 *<ul>
 * <li>materialId:物品id</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>communityId:园区id</li>
 *</ul>
 */
public class GetWarehouseMaterialPurchaseHistoryCommand {
    private Long materialId;
    private Long ownerId;
    private String ownerType;
    private Long communityId;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
