package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>catalogId: 目录id</li>
 * <li>catalogName: 目录名称</li>
 * <li>scopes: 选择对象列表 参考{@link com.everhomes.rest.filemanagement.FileCatalogScopeDTO}</li>
 * </ul>
 */
public class UpdateFileCatalogCommand {

    private Long catalogId;

    private String catalogName;

    @ItemType(FileCatalogScopeDTO.class)
    private List<FileCatalogScopeDTO> scopes;

    public UpdateFileCatalogCommand() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
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
