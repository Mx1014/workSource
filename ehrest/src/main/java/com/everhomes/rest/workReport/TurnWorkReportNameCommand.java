package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>reportStatus: 汇报状态 0-无效 1-未启用 2-启用 参考{@link com.everhomes.rest.workReport.WorkReportStatus}</li>
 * </ul>
 */
public class TurnWorkReportNameCommand {

    private Long reportId;

    private Byte reportStatus;

    public TurnWorkReportNameCommand() {
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Byte getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Byte reportStatus) {
        this.reportStatus = reportStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
