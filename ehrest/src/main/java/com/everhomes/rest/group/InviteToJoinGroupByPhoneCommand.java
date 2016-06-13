// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>phones: 被邀请加入group的用户手机号列表</li>
 * <li>invitationText: 邀请用户时填写的说明文本</li>
 * </ul>
 */
public class InviteToJoinGroupByPhoneCommand {
    @NotNull
    private Long groupId;
    
    @ItemType(String.class)
    private List<String> phones;
    
    private String invitationText;

    public InviteToJoinGroupByPhoneCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
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
