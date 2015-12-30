// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * <li>requestText: 申请为管理员时填写的说明文本</li>
 * </ul>
 */
public class RequestAdminRoleCommand {
    @NotNull
    private Long groupId;
    
    private String requestText;
    
    public RequestAdminRoleCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
