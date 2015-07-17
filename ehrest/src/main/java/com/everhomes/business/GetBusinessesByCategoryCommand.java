package com.everhomes.business;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>categoryId: 商家类别id</li>
 * </ul>
 */

public class GetBusinessesByCategoryCommand{
    @NotNull
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
