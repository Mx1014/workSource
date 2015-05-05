// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>contentCategory: 内容类型</li>
 * <li>actionCategory: 动作类型，对应以前的serviceType</li>
 * <li>visibilityScope: 可见性范围类型，{@link com.everhomes.visibility.VisibilityScope}</li>
 * <li>visibilityScopeId: 可见性范围类型对应的ID</li>
 * <li>pageOffset: 偏移量</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListTopicCommand {
    private Long forumId;
    private Long contentCategory;
    private Long actionCategory;
    private Byte visibilityScope;
    private Long visibilityScopeId;
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
    
    public Byte getVisibilityScope() {
		return visibilityScope;
	}

	public void setVisibilityScope(Byte visibilityScope) {
		this.visibilityScope = visibilityScope;
	}

	public Long getVisibilityScopeId() {
		return visibilityScopeId;
	}

	public void setVisibilityScopeId(Long visibilityScopeId) {
		this.visibilityScopeId = visibilityScopeId;
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
