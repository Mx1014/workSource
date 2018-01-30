package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>catalogs: 目录列表，参考{@link com.everhomes.rest.filemanagement.FileCatalogDTO}</li>
 * </ul>
 */
public class ListFileCatalogResponse {

    private Long nextPageAnchor;

    @ItemType(FileCatalogDTO.class)
    private List<FileCatalogDTO> catalogs;

    public ListFileCatalogResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<FileCatalogDTO> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<FileCatalogDTO> catalogs) {
        this.catalogs = catalogs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
