package com.everhomes.rest.workReport;

import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>reportName: 工作汇报名称</li>
 * <li>reportStatus: 汇报状态 0-无效 1-未启用 2-启用 参考{@link com.everhomes.rest.workReport.WorkReportStatus}</li>
 * <li>modifyFlag: 修改属性</li>
 * <li>deleteFlag: 删除属性</li>
 * <li>visibleRange: 可见范围 参考{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>reportType: 工作汇报类型 0-每日 1-每周 2-每月 参考{@link com.everhomes.rest.workReport.WorkReportType}</li>
 * <li>formOriginId: 关联表单id</li>
 * </ul>
 */
public class WorkReportDTO {

    private Long reportId;

    private String reportName;

    private Byte reportStatus;

    private Byte modifyFlag;

    private Byte deleteFlag;

    private OrganizationDTO visibleRange;

    private Byte reportType;

    private Long formOriginId;

    public WorkReportDTO() {
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

    public Byte getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Byte reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Byte getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Byte modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public OrganizationDTO getVisibleRange() {
        return visibleRange;
    }

    public void setVisibleRange(OrganizationDTO visibleRange) {
        this.visibleRange = visibleRange;
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
