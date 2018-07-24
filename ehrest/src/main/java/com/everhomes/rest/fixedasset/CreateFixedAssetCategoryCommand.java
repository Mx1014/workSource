package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 公司ID，必填</li>
 * <li>parentId: 父级分类的ID，一级分类不用传</li>
 * <li>categoryName: 分类名称，必填</li>
 * </ul>
 */
public class CreateFixedAssetCategoryCommand {
    private String ownerType;
    private Long ownerId;
    private Integer parentId;
    private String categoryName;

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
