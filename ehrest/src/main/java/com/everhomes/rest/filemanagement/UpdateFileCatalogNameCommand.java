package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

public class UpdateFileCatalogNameCommand {

    private Long catalogId;

    private String catalogName;

    public UpdateFileCatalogNameCommand() {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
