package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 表单id</li>
 * <li>organizationId: 公司id</li>
 * <li>detailId: 员工id</li>
 * <li>isExport: 0-no 1-yes</li>
 * </ul>
 */
public class GetArchivesEmployeeCommand {

    private Long formOriginId;

    private Long organizationId;

    private Long detailId;

    private Integer isExport;

    public GetArchivesEmployeeCommand() {
    }

    public GetArchivesEmployeeCommand(Long formOriginId, Long organizationId, Long detailId, Integer isExport) {
        this.formOriginId = formOriginId;
        this.organizationId = organizationId;
        this.detailId = detailId;
        this.isExport = isExport;
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

    public Integer getIsExport() {
        return isExport;
    }

    public void setIsExport(Integer isExport) {
        this.isExport = isExport;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
