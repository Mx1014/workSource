package com.everhomes.rest.banner.admin;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>keyword: 查询banner关键字</li>
 * <li>pageOffset: 当前页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListBannersAdminCommand {
    
    @NotNull
    private String keyword;
    
    private Integer pageOffset;
    
    private Integer pageSize;
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
