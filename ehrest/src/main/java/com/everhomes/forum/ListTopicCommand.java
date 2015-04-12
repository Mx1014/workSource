package com.everhomes.forum;

import com.everhomes.util.StringHelper;

public class ListTopicCommand {
    private Long forumId;
    private Long contentCategory;
    private Long actionCategory;
    private Integer visibleScope;
    
    private Long pageOffset;
    private Long pageSize;
    
    public ListTopicCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
    }

    public Integer getVisibleScope() {
        return visibleScope;
    }

    public void setVisibleScope(Integer visibleScope) {
        this.visibleScope = visibleScope;
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
