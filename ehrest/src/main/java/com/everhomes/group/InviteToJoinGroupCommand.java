// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>phone: 被邀请加入group的用户手机号</li>
 * <li>invitationText: 邀请用户时填写的说明文本</li>
 * </ul>
 */
public class InviteToJoinGroupCommand {
    @NotNull
    private Long groupId;
    
    private String phone;
    
    private String invitationText;

    public InviteToJoinGroupCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

	public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
