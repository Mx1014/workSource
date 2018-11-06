package com.everhomes.rest.filemanagement;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 目录id</li>
 * <li>name: 目录名称</li>
 * <li>scopes: 可见范围列表 参考{@link com.everhomes.rest.filemanagement.FileCatalogScopeDTO}</li>
 * <li>downloadPermission: 下载权限 0-拒绝下载 1-允许下载</li>
 * <li>iconUrl: 图标url</li>
 * <li>createTime: 创建时间</li>
 * <li>contents: 可见范围列表 参考{@link com.everhomes.rest.filemanagement.FileContentDTO}</li>
 * <li>operatorName: 更新人</li>
 * <li>updateTime: 更新时间</li>
 * </ul>
 */
public class FileCatalogDTO {

    private Long id;

    private String name;

    @ItemType(FileCatalogScopeDTO.class)
    private List<FileCatalogScopeDTO> scopes;

    private Byte downloadPermission;

    private String iconUrl;

    private Timestamp createTime;

    private List<FileContentDTO> contents;

    private String operatorName;

    private Timestamp updateTime;

    private String identify;

    public FileCatalogDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FileCatalogScopeDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<FileCatalogScopeDTO> scopes) {
        this.scopes = scopes;
    }

    public Byte getDownloadPermission() {
        return downloadPermission;
    }

    public void setDownloadPermission(Byte downloadPermission) {
        this.downloadPermission = downloadPermission;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<FileContentDTO> getContents() {
        return contents;
    }

    public void setContents(List<FileContentDTO> contents) {
        this.contents = contents;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}
