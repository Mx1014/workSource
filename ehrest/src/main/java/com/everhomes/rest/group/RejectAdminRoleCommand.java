// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * <li>userId: 要被拒绝成员管理员角色的用户ID</li>
 * <li>rejectText: 拒绝用户时填写的说明文本</li>
 * </ul>
 */
public class RejectAdminRoleCommand {
    @NotNull
    private Long groupId;
    
    @NotNull
    private Long userId;
    
    private String rejectText;
    
    public RejectAdminRoleCommand() {
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

	public String getRejectText() {
        return rejectText;
    }

    public void setRejectText(String rejectText) {
        this.rejectText = rejectText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
