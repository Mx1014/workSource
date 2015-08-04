package com.everhomes.business;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>categoryId: 商家类别id</li>
 * <li>pageOffset: 当前页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */

public class GetBusinessesByCategoryCommand{
    @NotNull
    private Long categoryId;
    private Integer pageOffset;
    private Integer pageSize;
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
