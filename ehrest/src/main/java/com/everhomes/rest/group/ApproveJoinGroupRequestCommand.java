// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>userId: 被批准加入group的用户ID</li>
 * </ul>
 */
public class ApproveJoinGroupRequestCommand {
    @NotNull
    private Long groupId;
    
    private Long userId;

    public ApproveJoinGroupRequestCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
