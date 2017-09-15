// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>userIds: 被踢出group的用户ID列表</li>
 * <li>revokeText: 踢出用户时填写的说明文本</li>
 * </ul>
 */
public class RevokeGroupMemberListCommand {
    @NotNull
    private Long groupId;

    @ItemType(Long.class)
    private List<Long> userIds;

    private String revokeText;

    public RevokeGroupMemberListCommand() {
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
