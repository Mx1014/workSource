// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumIdList: 论坛ID列表</li>
 * <li>communityId: 当前小区ID，仅社区圈有效</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListTopicByForumCommand {
    @ItemType(Long.class)
    private List<Long> forumIdList;
    
    private Long communityId;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    public ListTopicByForumCommand() {
    }
	
    public List<Long> getForumIdList() {
        return forumIdList;
    }

    public void setForumIdList(List<Long> forumIdList) {
        this.forumIdList = forumIdList;
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
