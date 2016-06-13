// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicId: 帖子ID</li>
 * <li>assignedFlag: 设置热帖标记，参考{@link com.everhomes.rest.forum.PostAssignedFlag}</li>
 * <li>scopeCode: 范围类型，参考{@link com.everhomes.rest.forum.ForumAssignedScopeCode}</li>
 * <li>scopeIds: 范围类型对应的ID</li>
 * </ul>
 */
public class AssignTopicScopeCommand {
    @NotNull
    private Long forumId;
    
    @NotNull
    private Long topicId;
    
    @NotNull
    private Byte assignedFlag;
    
    private Byte scopeCode;
    
    @ItemType(Long.class)
    private List<Long> scopeIds;
    
    public AssignTopicScopeCommand() {
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

    public Byte getAssignedFlag() {
        return assignedFlag;
    }

    public void setAssignedFlag(Byte assignedFlag) {
        this.assignedFlag = assignedFlag;
    }

    public Byte getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(Byte scopeCode) {
        this.scopeCode = scopeCode;
    }

    public List<Long> getScopeIds() {
        return scopeIds;
    }

    public void setScopeIds(List<Long> scopeIds) {
        this.scopeIds = scopeIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
