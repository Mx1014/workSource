// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicUuid: 帖子UUID</li>
 * </ul>
 */
public class GetTopicCommand {
    private Long forumId;
    private String topicUuid;
    
    public GetTopicCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public String getTopicUuid() {
        return topicUuid;
    }

    public void setTopicUuid(String topicUuid) {
        this.topicUuid = topicUuid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
