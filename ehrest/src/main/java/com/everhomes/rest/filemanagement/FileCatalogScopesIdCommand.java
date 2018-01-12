package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class FileCatalogScopesIdCommand {

    private Long catalogId;

    @ItemType(Long.class)
    private List<Long> sourceId;

    public FileCatalogScopesIdCommand() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public List<Long> getSourceId() {
        return sourceId;
    }

    public void setSourceId(List<Long> sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
