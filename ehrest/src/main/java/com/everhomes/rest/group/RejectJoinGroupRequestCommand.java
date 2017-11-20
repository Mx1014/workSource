// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>groupId: group id</li>
 *     <li>userId: 被踢出group的用户ID</li>
 *     <li>rejectText: 拒绝用户时填写的说明文本</li>
 *     <li>organizationId: 行业协会专用</li>
 * </ul>
 */
public class RejectJoinGroupRequestCommand {
    @NotNull
    private Long groupId;

    private Long userId;

    private String rejectText;

    private Long organizationId;

    public RejectJoinGroupRequestCommand() {
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
