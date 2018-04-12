package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>catalogId: 目录id</li>
 * </ul>
 */
public class FileCatalogIdCommand {

    private Long catalogId;

    public FileCatalogIdCommand() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
