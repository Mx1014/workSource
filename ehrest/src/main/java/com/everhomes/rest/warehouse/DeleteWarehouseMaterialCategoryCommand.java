package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>categoryId: 物品分类id</li>
 *     <li>ownerType: 物品分类所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 物品分类所属类型id</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class DeleteWarehouseMaterialCategoryCommand {

    private String ownerType;

    private Long ownerId;

    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
