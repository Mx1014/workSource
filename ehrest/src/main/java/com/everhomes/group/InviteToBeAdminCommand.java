// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * <li>phone: 被邀请人的手机号</li>
 * <li>invitationText: 邀请内容</li>
 * </ul>
 */
public class InviteToBeAdminCommand {
    @NotNull
    private Long groupId;
    
    @NotNull
    private String phone;
    
    private String invitationText;
    
    public InviteToBeAdminCommand() {
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
