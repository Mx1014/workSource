package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>返回值:
 * <li>catalogs: 目录列表 参考{@link com.everhomes.rest.filemanagement.FileCatalogDTO}</li>
 * </ul>
 */
public class ListAllFlodersResponse {
    private List<FileCatalogDTO> catalogs;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    public List<FileCatalogDTO> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<FileCatalogDTO> catalogs) {
        this.catalogs = catalogs;
    }
}
