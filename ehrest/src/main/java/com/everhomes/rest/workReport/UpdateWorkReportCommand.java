package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>visibleRangeId: 可见范围id</li>
 * <li>reportType: 汇报类型 0-每日 1-每周 2-每月 参考{@link com.everhomes.rest.workReport.WorkReportType}</li>
 * <li>formOriginId: 表单id</li>
 * </ul>
 */
public class UpdateWorkReportCommand {

    private Long reportId;

    private Long visibleRangeId;

    private Byte reportType;

    private Long formOriginId;

    public UpdateWorkReportCommand() {
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getVisibleRangeId() {
        return visibleRangeId;
    }

    public void setVisibleRangeId(Long visibleRangeId) {
        this.visibleRangeId = visibleRangeId;
    }

    public Byte getReportType() {
        return reportType;
    }

    public void setReportType(Byte reportType) {
        this.reportType = reportType;
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
