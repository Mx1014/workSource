package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerId: 场景id</li>
 * <li>ownerType: 场景类型</li>
 * <li>catalogName: 目录名称</li>
 * <li>scopes: 选择对象列表 参考{@link com.everhomes.rest.filemanagement.FileCatalogScopeDTO}</li>
 * </ul>
 */
public class AddFileCatalogCommand {

    private Long ownerId;

    private String ownerType;

    private String catalogName;

    @ItemType(FileCatalogScopeDTO.class)
    private List<FileCatalogScopeDTO> scopes;

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

    public List<FileCatalogScopeDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<FileCatalogScopeDTO> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
