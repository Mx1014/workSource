// @formatter:off
package com.everhomes.category;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 类型ID</li>
 * <li>parentId: 父类型ID</li>
 * <li>name: 类型名称</li>
 * <li>path: 类型路径</li>
 * <li>defaultOrder: 默认顺序索引</li>
 * <li>status: 状态，{@link com.everhomes.category.CategoryAdminStatus}</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteTime: 删除时间</li>
 * </ul>
 */
public class CategoryDTO {
    private Long     id;
    private Long     parentId;
    private String   name;
    private String   path;
    private Integer  defaultOrder;
    private Byte     status;
    
    private Timestamp createTime;
    private Timestamp deleteTime;

    public CategoryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }   
}
