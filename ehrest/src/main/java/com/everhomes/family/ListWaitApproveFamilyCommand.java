// @formatter:off
package com.everhomes.family;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListWaitApproveFamilyCommand {

    private Long pageOffset;
    
    private Long pageSize;

    public ListWaitApproveFamilyCommand() {
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
