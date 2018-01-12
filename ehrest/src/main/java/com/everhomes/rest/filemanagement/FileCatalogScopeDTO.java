package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cateLogId: 目录id</li>
 * <li>sourceId: 选择对象id</li>
 * <li>sourceDescription: 选择对象描述</li>
 * </ul>
 */
public class FileCatalogScopeDTO {

    private Long cateLogId;

    private Long sourceId;

    private String sourceDescription;

    public FileCatalogScopeDTO() {
    }

    public Long getCateLogId() {
        return cateLogId;
    }

    public void setCateLogId(Long cateLogId) {
        this.cateLogId = cateLogId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
