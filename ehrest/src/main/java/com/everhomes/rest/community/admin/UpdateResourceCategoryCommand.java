// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>id: 类型id</li>
 * <li>name: 类型名称</li>
 * </ul>
 */
public class UpdateResourceCategoryCommand {

    private Long id;

    private String name;

    public UpdateResourceCategoryCommand() {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
