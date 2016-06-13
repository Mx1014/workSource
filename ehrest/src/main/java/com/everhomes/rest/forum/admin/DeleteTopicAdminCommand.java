// @formatter:off
package com.everhomes.rest.forum.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicId: 帖子ID</li>
 * </ul>
 */
public class DeleteTopicAdminCommand {
    private Long forumId;
    private Long topicId;
    
    public DeleteTopicAdminCommand() {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
