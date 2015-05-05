// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>topicId: 帖子ID</li>
 * <li>targetForumId: 目的论坛ID</li>
 * <li>visibleFlag: 可见性标记</li>
 * </ul>
 */
public class ForwardTopicCommand {
    private Long topicId;
    private Long targetForumId;
    private Integer visibleFlag;
    
    public ForwardTopicCommand() {
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getTargetForumId() {
        return targetForumId;
    }

    public void setTargetForumId(Long targetForumId) {
        this.targetForumId = targetForumId;
    }

    public Integer getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Integer visibleFlag) {
        this.visibleFlag = visibleFlag;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
