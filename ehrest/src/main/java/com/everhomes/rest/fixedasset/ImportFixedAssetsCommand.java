// @formatter:off
package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

/**
 * <ul>参数:
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 默认EhOrganizations</li>
 * <li>ownerId: 公司ID，必填</li>
 * <li>fixedAssetCategoryId: 分类ID</li>
 * <li>attachment: 文件</li>
 * </ul>
 */
public class ImportFixedAssetsCommand {
    private Integer namespaceId;
    private String ownerType;

    private Long ownerId;

    private Integer fixedAssetCategoryId;


    public ImportFixedAssetsCommand() {

    }

    public ImportFixedAssetsCommand(String ownerType, Long ownerId) {
        super();
        this.ownerType = ownerType;
        this.ownerId = ownerId;
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

    public Integer getFixedAssetCategoryId() {
        return fixedAssetCategoryId;
    }

    public void setFixedAssetCategoryId(Integer fixedAssetCategoryId) {
        this.fixedAssetCategoryId = fixedAssetCategoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
