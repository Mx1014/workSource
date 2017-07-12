package com.everhomes.rest.blacklist;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>privilegeId: 权限id</li>
 * <li>privilegeName: 权限名称</li>
 * <li>description: 描述</li>
 * </ul>
 */
public class BlacklistPrivilegeDTO {

    private Long privilegeId;

    private String privilegeName;

    private String description;

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
