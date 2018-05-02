package com.everhomes.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: EhOrganizations</li>
 * <li>ownerId: 公司ID</li>
 * <li>categoryName: 分类名称</li>
 * <li>selfCategoryId: 当前的分类ID</li>
 * </ul>
 */
public class CheckFixedAssetCategoryNameExistRequest {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Integer parentId;
    private String categoryName;
    private Integer selfCategoryId;

    public CheckFixedAssetCategoryNameExistRequest() {

    }

    public CheckFixedAssetCategoryNameExistRequest(Integer namespaceId, String ownerType, Long ownerId, Integer parentId, String categoryName) {
        this.namespaceId = namespaceId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.parentId = parentId;
        this.categoryName = categoryName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

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

    public Integer getSelfCategoryId() {
        return selfCategoryId;
    }

    public void setSelfCategoryId(Integer selfCategoryId) {
        this.selfCategoryId = selfCategoryId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
