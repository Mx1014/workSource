package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>catalogId: catalogId</li>
 * <li>sourceIds: 选择对象id列表</li>
 * </ul>
 */
public class FileCatalogScopesIdCommand {

    private Long catalogId;

    @ItemType(Long.class)
    private List<Long> sourceIds;

    public FileCatalogScopesIdCommand() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public List<Long> getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(List<Long> sourceIds) {
        this.sourceIds = sourceIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
