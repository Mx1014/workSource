package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>moduleId: 业务模块id</li>
 * </ul>
 */
public class ListServiceModuleAdministratorsCommand {

    private Long organizationId;

    private Long moduleId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
