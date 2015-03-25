// @formatter:off
package com.everhomes.region;

import com.everhomes.util.StringHelper;

public class ListRegionCommand {
    private Long parentId;
    private Byte scope;
    private Byte status;
    private String sortBy;
    private Byte sortOrder;
    
    public ListRegionCommand() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getScope() {
        return scope;
    }

    public void setScope(Byte scope) {
        this.scope = scope;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Byte getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Byte sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
