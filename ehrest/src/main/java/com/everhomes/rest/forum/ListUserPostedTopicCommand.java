// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

public class ListUserPostedTopicCommand {
    private Long pageOffset;
    private Long pageSize;
    
    public ListUserPostedTopicCommand() {
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
