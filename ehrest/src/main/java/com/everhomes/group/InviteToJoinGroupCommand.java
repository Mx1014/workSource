// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>userId: 被邀请加入group的用户ID</li>
 * <li>invitationText: 邀请用户时填写的说明文本</li>
 * </ul>
 */
public class InviteToJoinGroupCommand {
    @NotNull
    private Long groupId;
    
    private Long userId;
    
    private String invitationText;

    public InviteToJoinGroupCommand() {
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

	public String getInvitationText() {
        return invitationText;
    }

    public void setInvitationText(String invitationText) {
        this.invitationText = invitationText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
