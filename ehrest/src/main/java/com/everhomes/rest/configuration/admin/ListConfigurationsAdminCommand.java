package com.everhomes.rest.configuration.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageOffset: 当前页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListConfigurationsAdminCommand {
    
    private Integer pageOffset;
    
    private Integer pageSize;
    
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
