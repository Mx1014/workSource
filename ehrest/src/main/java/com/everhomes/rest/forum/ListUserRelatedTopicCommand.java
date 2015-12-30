// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 当前小区ID</li>
 * <li>pageAnchor: 开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListUserRelatedTopicCommand {
    private Long communityId;
    private Long pageAnchor;
    private Integer pageSize;
    
    
    public ListUserRelatedTopicCommand() {
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
