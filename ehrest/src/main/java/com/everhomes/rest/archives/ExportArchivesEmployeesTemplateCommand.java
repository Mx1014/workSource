package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>formOriginId: 表单id(调用模板或没有表单时为0)</li>
 * </ul>
 */
public class ExportArchivesEmployeesTemplateCommand {

    private Long organizationId;

    private Long formOriginId;

    public ExportArchivesEmployeesTemplateCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
