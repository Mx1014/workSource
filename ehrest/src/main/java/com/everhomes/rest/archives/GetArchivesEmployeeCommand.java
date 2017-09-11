package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 表单id</li>
 * <li>organizationId: 公司id</li>
 * <li>detailId: 员工id</li>
 * </ul>
 */
public class GetArchivesEmployeeCommand {

    private Long formOriginId;

    private Long organizationId;

    private Long detailId;

    public GetArchivesEmployeeCommand() {
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
