package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>departmentId: 部门 id</li>
 * <li>formOriginId: 表单 id</li>
 * </ul>
 */
public class ImportArchivesEmployeesCommand {

    private Long organizationId;

    private Long departmentId;

    private Long formOriginId;

    public ImportArchivesEmployeesCommand() {
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

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
