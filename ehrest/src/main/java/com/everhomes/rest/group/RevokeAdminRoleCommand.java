// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * <li>userId: 要被去掉管理员角色的用户ID</li>
 * <li>revokeText: 取消管理员角色时填写的说明文本</li>
 * </ul>
 */
public class RevokeAdminRoleCommand {
    @NotNull
    private Long groupId;
    
    @NotNull
    private Long userId;
    
    private String revokeText;
    
    public RevokeAdminRoleCommand() {
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

	public String getRevokeText() {
        return revokeText;
    }

    public void setRevokeText(String revokeText) {
        this.revokeText = revokeText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
