package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>catalogId: 目录id</li>
 * <li>catalogName: 目录名称</li>
 * </ul>
 */
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
