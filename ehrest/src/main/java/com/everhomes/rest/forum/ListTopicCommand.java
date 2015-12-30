// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>visibilityScope: 可见性范围类型，仅社区圈有效，{@link com.everhomes.rest.visibility.VisibilityScope}</li>
 * <li>communityId: 当前小区ID，仅社区圈有效</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListTopicCommand {
    private Long forumId;
    private Byte visibilityScope;
    private Long communityId;
    private Long pageAnchor;
    private Integer pageSize;
    
    public ListTopicCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }
    
    public Byte getVisibilityScope() {
		return visibilityScope;
	}

	public void setVisibilityScope(Byte visibilityScope) {
		this.visibilityScope = visibilityScope;
	}
	
    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
