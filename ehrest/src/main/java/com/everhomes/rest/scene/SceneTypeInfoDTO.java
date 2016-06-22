// @formatter:off
package com.everhomes.rest.scene;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 场景ID</li>
 * <li>namespaceId: 域名空间的ID</li>
 * <li>name: 场景逻辑名称（用于作逻辑、不作显示）</li>
 * <li>displayName: 显示名称</li>
 * <li>createTime: 创建时间</li>
 * <li>parentId: 父亲ID</li>
 * </ul>
 */
public class SceneTypeInfoDTO {
    private Long id;
    private Integer namespaceId;
    private String name;
    private String displayName;
    private Timestamp createTime;
    private Long parentId;
    
    public SceneTypeInfoDTO() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
