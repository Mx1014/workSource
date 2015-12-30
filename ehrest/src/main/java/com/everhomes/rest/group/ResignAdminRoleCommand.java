// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId: group id</li>
 * </ul>
 */
public class ResignAdminRoleCommand {
    @NotNull
    private Long groupId;
    
    public ResignAdminRoleCommand() {
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
