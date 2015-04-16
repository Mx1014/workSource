// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListMemberInRoleCommand {
    @NotNull
    private Long groupId;
    
    @NotNull
    private Long roleId;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    public ListMemberInRoleCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
