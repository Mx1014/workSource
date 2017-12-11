package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>reportType: 汇报类型 0-每日 1-每周 2-每月 参考{@link com.everhomes.rest.workReport.WorkReportType}</li>
 * <li>formOriginId: 表单id</li>
 * <li>formVersion: 表单版本</li>
 * <li>scopes: 可见范围 参考{@link com.everhomes.rest.workReport.WorkReportScopeMapDTO}</li>
 * </ul>
 */
public class UpdateWorkReportCommand {

    private Long reportId;

    private Byte reportType;

    private Long formOriginId;

    private Long formVersion;

    @ItemType(WorkReportScopeMapDTO.class)
    private List<WorkReportScopeMapDTO> scopes;

    public UpdateWorkReportCommand() {
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
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

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public List<WorkReportScopeMapDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<WorkReportScopeMapDTO> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
