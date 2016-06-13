// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * <li>userId: 被邀请人的用户ID</li>
 * <li>invitationText: 邀请内容</li>
 * </ul>
 */
public class InviteToBeAdminCommand {
    @NotNull
    private Long groupId;
    
    @NotNull
    private long userId;
    
    private String invitationText;
    
    public InviteToBeAdminCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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
