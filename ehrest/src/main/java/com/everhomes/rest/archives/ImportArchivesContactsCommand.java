package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>departmentId: 部门 id</li>
 * </ul>
 */
public class ImportArchivesContactsCommand {

    private Long organizationId;

    private Long departmentId;

    public ImportArchivesContactsCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
