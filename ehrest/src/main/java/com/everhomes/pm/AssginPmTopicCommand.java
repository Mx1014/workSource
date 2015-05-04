// @formatter:off
package com.everhomes.pm;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛Id</li>
 * <li>topicId: 帖子ID</li>
 * <li>userId: 接收任务的用户Id</li>
 * <li>status: 任务状态，0-未处理，1-处理中，2-已处理，参考{@link com.everhomes.pm.PmTaskStatus}</li>
 * </ul>
 */
public class AssginPmTopicCommand {
    private Long forumId;
    private Long topicId;
    private Long userId;
    private Byte status;
    
    public AssginPmTopicCommand() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
