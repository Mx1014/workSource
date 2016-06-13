// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>userIds: 被邀请加入group的用户ID列表</li>
 * <li>invitationText: 邀请用户时填写的说明文本</li>
 * </ul>
 */
public class InviteToJoinGroupCommand {
    @NotNull
    private Long groupId;
    
    @ItemType(Long.class)
    private List<Long> userIds;
    
    private String invitationText;

    public InviteToJoinGroupCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
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
