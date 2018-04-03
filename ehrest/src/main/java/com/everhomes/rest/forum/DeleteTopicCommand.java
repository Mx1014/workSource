// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>currentOrgId: 当前所在机构</li>
 * <li>ownerType: 项目范围</li>
 * <li>ownerId: 项目id</li>
 * <li>topicId: 帖子ID</li>
 * </ul>
 */
public class DeleteTopicCommand {

    private Long currentOrgId;
    private String ownerType;
    private Long ownerId;

    private Long forumId;
    private Long topicId;
    
    public DeleteTopicCommand() {
    }

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
