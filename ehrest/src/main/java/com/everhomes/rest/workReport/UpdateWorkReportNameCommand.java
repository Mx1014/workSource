package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>reportName: 工作汇报名称</li>
 * </ul>
 */
public class UpdateWorkReportNameCommand {

    private Long reportId;

    private String reportName;

    public UpdateWorkReportNameCommand() {
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
