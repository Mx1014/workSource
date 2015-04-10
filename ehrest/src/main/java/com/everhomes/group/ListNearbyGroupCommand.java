// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

public class ListNearbyGroupCommand {
    private Long pageOffset;
    
    public ListNearbyGroupCommand() {
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
