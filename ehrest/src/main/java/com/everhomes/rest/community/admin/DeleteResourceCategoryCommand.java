// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>id: 类型id</li>
 * </ul>
 */
public class DeleteResourceCategoryCommand {

    private Long id;

    public DeleteResourceCategoryCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
