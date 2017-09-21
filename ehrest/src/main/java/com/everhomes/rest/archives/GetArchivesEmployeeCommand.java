package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 表单id</li>
 * <li>organizationId: 公司id</li>
 * <li>detailId: 员工id</li>
 * <li>dismiss: 离职员工(在职员工无需传递此参数)</li>
 * </ul>
 */
public class GetArchivesEmployeeCommand {

    private Long formOriginId;

    private Long organizationId;

    private Long detailId;

    private String dismiss;

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

    public String getDismiss() {
        return dismiss;
    }

    public void setDismiss(String dismiss) {
        this.dismiss = dismiss;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
