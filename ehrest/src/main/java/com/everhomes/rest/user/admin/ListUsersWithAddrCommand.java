package com.everhomes.rest.user.admin;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>pageOffset: 当前页码</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListUsersWithAddrCommand {
private Integer pageOffset;
    
    private Integer pageSize;
    
    public ListUsersWithAddrCommand() {
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
