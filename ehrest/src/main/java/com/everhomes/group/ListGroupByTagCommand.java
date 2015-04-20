// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListGroupByTagCommand {
    private Byte searchVisibilityScope;
    private Long searchVisibilityScopeId;
    
    @NotNull
    private String tag;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    public ListGroupByTagCommand() {
    }
    
    public Byte getSearchVisibilityScope() {
        return searchVisibilityScope;
    }

    public void setSearchVisibilityScope(Byte searchVisibilityScope) {
        this.searchVisibilityScope = searchVisibilityScope;
    }

    public Long getSearchVisibilityScopeId() {
        return searchVisibilityScopeId;
    }

    public void setSearchVisibilityScopeId(Long searchVisibilityScopeId) {
        this.searchVisibilityScopeId = searchVisibilityScopeId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
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
