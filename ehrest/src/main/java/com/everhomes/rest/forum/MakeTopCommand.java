// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicId: 帖子ID</li>
 * <li>topFlag: 置顶标记</li>
 * </ul>
 */
public class MakeTopCommand {
    private Long forumId;
    private Long topicId;
    private Byte topFlag;
    
    public MakeTopCommand() {
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

    public Byte getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(Byte topFlag) {
        this.topFlag = topFlag;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
