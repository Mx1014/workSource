package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>reportValId: 工作汇报单id</li>
 * <li>organizationId: 公司企业id</li>
 * </ul>
 */
public class WorkReportDetailActionData {
    private Long reportId;

    private Long reportValId;

    private Long organizationId;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getReportValId() {
        return reportValId;
    }

    public void setReportValId(Long reportValId) {
        this.reportValId = reportValId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

