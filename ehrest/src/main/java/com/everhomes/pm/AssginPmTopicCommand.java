// @formatter:off
package com.everhomes.pm;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛Id</li>
 * <li>communityId: 小区Id</li>
 * <li>topicIds: 帖子ID列表</li>
 * <li>userId: 接收任务的用户Id</li>
 * <li>status: 任务状态，0-未处理，1-处理中，2-已处理，参考{@link com.everhomes.pm.PmTaskStatus}</li>
 * </ul>
 */
public class AssginPmTopicCommand {
    private Long forumId;
    private Long communityId;
    @ItemType(Long.class)
    private List<Long> topicIds;
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

    public List<Long> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Long> topicIds) {
        this.topicIds = topicIds;
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
