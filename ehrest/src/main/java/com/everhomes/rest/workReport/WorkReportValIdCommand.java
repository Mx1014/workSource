package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportValId: 工作汇报单id</li>
 * <li>reportId: 工作汇报id</li>
 * </ul>
 */
public class WorkReportValIdCommand {

    private Long reportValId;

    private Long reportId;

    public WorkReportValIdCommand() {
    }

    public Long getReportValId() {
        return reportValId;
    }

    public void setReportValId(Long reportValId) {
        this.reportValId = reportValId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
