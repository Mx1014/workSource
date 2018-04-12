package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 场景id</li>
 * <li>ownerType: 场景类型</li>
 * <li>catalogName: 目录名称</li>
 * </ul>
 */
public class AddFileCatalogCommand {

    private Long ownerId;

    private String ownerType;

    private String catalogName;

    public AddFileCatalogCommand() {
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

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
