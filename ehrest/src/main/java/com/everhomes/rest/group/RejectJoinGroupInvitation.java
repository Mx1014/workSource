// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>rejectText: 拒绝用户时填写的说明文本</li>
 * </ul>
 */
public class RejectJoinGroupInvitation {
    @NotNull
    private Long groupId;
    
    private String rejectText;

    public RejectJoinGroupInvitation() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
