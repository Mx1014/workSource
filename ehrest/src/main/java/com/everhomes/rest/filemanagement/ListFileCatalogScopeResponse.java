package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>scopes: 可见人员 参考{@link com.everhomes.rest.filemanagement.FileCatalogScopeDTO}</li>
 * </ul>
 */
public class ListFileCatalogScopeResponse {

    private Long nextPageAnchor;

    @ItemType(FileCatalogScopeDTO.class)
    private List<FileCatalogScopeDTO> scopes;

    public ListFileCatalogScopeResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
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
