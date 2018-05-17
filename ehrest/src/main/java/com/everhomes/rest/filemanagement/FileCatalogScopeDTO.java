package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>catalogId: 目录id</li>
 * <li>sourceId: 选择对象id</li>
 * <li>sourceType: 选择对象类型 参考@{@link com.everhomes.rest.uniongroup.UniongroupTargetType}</li>
 * <li>sourceDescription: 选择对象描述</li>
 * <li>downloadPermission: 下载权限：0-拒绝下载, 1-允许下载</li>
 * </ul>
 */
public class FileCatalogScopeDTO {

    private Long catalogId;

    private Long sourceId;

    private String sourceType;

    private String sourceDescription;

    private Byte downloadPermission;

    public FileCatalogScopeDTO() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }

    public Byte getDownloadPermission() {
        return downloadPermission;
    }

    public void setDownloadPermission(Byte downloadPermission) {
        this.downloadPermission = downloadPermission;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
