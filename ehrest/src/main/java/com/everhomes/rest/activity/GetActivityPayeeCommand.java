// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>categoryId: 应用ID</li>
 * </ul>
 */
public class GetActivityPayeeCommand {
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
