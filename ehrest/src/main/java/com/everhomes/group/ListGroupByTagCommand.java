// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListGroupByTagCommand {
    private Long regionId;
    
    @NotNull
    private String tag;
    private Long pageOffset;
    
    public ListGroupByTagCommand() {
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
