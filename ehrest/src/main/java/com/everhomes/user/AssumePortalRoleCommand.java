// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AssumePortalRoleCommand {
    
    @NotNull
    private Long roleId;
    
    public AssumePortalRoleCommand() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
