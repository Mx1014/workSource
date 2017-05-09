// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

public class OfficialActivityActionData {

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
