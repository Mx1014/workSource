package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>catalogId: 目录id</li>
 * <li>scopes: 选择对象列表 参考{@link com.everhomes.rest.filemanagement.FileCatalogScopeDTO}</li>
 * </ul>
 */
public class AddFileCatalogScopesCommand {

    private Long catalogId;

    private List<FileCatalogScopeDTO> scopes;

    public AddFileCatalogScopesCommand() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
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
