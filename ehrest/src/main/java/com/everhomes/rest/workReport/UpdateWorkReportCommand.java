package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>reportType: 汇报类型 0-每日 1-每周 2-每月 参考{@link com.everhomes.rest.workReport.WorkReportType}</li>
 * <li>validitySetting: 提交时间设置 参考{@link com.everhomes.rest.workReport.ReportValiditySettingDTO}</li>
 * <li>rxMsgType: 接收提醒类型 0-即时 1-汇总</li>
 * <li>rxMsgSetting: 接收时间设置 参考{@link com.everhomes.rest.workReport.ReportMsgSettingDTO}</li>
 * <li>auMsgType: 提交提醒类型 0-不提醒 1-提醒</li>
 * <li>auMsgSetting: 提交时间设置 参考{@link com.everhomes.rest.workReport.ReportMsgSettingDTO}</li>
 * <li>formOriginId: 表单id</li>
 * <li>formVersion: 表单版本</li>
 * <li>scopes: 可见范围 参考{@link com.everhomes.rest.workReport.WorkReportScopeMapDTO}</li>
 * </ul>
 */
public class UpdateWorkReportCommand {

    private Long reportId;

    private Byte reportType;

    private ReportValiditySettingDTO validitySetting;

    private Byte rxMsgType;

    private ReportMsgSettingDTO rxMsgSetting;

    private Byte auMsgType;

    private ReportMsgSettingDTO auMsgSetting;

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

    public ReportValiditySettingDTO getValiditySetting() {
        return validitySetting;
    }

    public void setValiditySetting(ReportValiditySettingDTO validitySetting) {
        this.validitySetting = validitySetting;
    }

    public Byte getRxMsgType() {
        return rxMsgType;
    }

    public void setRxMsgType(Byte rxMsgType) {
        this.rxMsgType = rxMsgType;
    }

    public ReportMsgSettingDTO getRxMsgSetting() {
        return rxMsgSetting;
    }

    public void setRxMsgSetting(ReportMsgSettingDTO rxMsgSetting) {
        this.rxMsgSetting = rxMsgSetting;
    }

    public Byte getAuMsgType() {
        return auMsgType;
    }

    public void setAuMsgType(Byte auMsgType) {
        this.auMsgType = auMsgType;
    }

    public ReportMsgSettingDTO getAuMsgSetting() {
        return auMsgSetting;
    }

    public void setAuMsgSetting(ReportMsgSettingDTO auMsgSetting) {
        this.auMsgSetting = auMsgSetting;
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
