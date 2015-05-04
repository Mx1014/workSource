// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicId: 帖子ID</li>
 * <li>pageOffset: 偏移量</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListTopicCommentCommand {
    private Long forumId;
    private Long topicId;
    private Long pageOffset;
    private Long pageSize;
    
    public ListTopicCommentCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
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
    
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
