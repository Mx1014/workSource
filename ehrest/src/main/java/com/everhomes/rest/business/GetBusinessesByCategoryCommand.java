package com.everhomes.rest.business;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>categoryId: 商家类别id</li>
 * <li>communityId: 小区id</li>
 * <li>pageOffset: 当前页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */

public class GetBusinessesByCategoryCommand{
    @NotNull
    private Long categoryId;
    @NotNull
    private Long communityId;
    private Integer pageOffset;
    private Integer pageSize;
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
