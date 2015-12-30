// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * </ul>
 */
public class GetAdminRoleStatusCommand {
    @NotNull
    private Long groupId;
    
    public GetAdminRoleStatusCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
