// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 当前页条数</li>
 * </ul>
 */
public class ListAllCommunitesCommand {
    // start from 1, page size is configurable at server side
    @NotNull
    private Long pageOffset;
    
    private Long pageSize;
    
    public ListAllCommunitesCommand() {
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
    
}
