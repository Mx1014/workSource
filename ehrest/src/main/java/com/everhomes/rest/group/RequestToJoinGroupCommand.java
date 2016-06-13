// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>inviterId：邀请人id</li>
 * <li>requestText：发起请求时可填写的说明文本</li>
 * </ul>
 */
public class RequestToJoinGroupCommand {
    @NotNull
    private Long groupId;
    
    private Long inviterId;
    
    private String requestText;

    public RequestToJoinGroupCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
