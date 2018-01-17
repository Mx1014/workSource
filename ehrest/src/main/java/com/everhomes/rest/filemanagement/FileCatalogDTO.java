package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 目录id</li>
 * <li>name: 目录名称</li>
 * <li>downloadPermission: 下载权限 0-拒绝下载 1-允许下载</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class FileCatalogDTO {

    private Long id;

    private String name;

    private Byte downloadPermission;

    private Timestamp createTime;

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
}
