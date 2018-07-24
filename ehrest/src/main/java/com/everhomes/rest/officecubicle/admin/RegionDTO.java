// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 区域ID</li>
 * <li>parentId: 父亲区域的ID</li>
 * <li>name: 区域名称</li>
 * <li>path: 区域路径，含层次关系，如/广东省/深圳市</li>
 * </ul>
 */
public class RegionDTO {
    private Long id;
    private Integer parentId;
    private String  name;
    private String  path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
