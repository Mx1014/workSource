package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>reportId: 工作汇报id</li>
 * <li>reportName: 工作汇报名称</li>
 * <li>reportType: 工作汇报类型 0-每日 1-每周 2-每月 参考{@link com.everhomes.rest.workReport.WorkReportType}</li>
 * <li>formOriginId: 关联表单id</li>
 * <li>formVersion: 关联表单版本</li>
 * <li>validitySetting: 提交时间设置 参考{@link com.everhomes.rest.workReport.ReportValiditySettingDTO}</li>
 * <li>status: 汇报状态 0-无效 1-未启用 2-启用 参考{@link com.everhomes.rest.workReport.WorkReportStatus}</li>
 * <li>scopes: 可见范围 参考{@link WorkReportScopeMapDTO}</li>
 * <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改 {@link com.everhomes.rest.workReport.AttitudeFlag}</li>
 * <li>deleteFlag: 是否可删除 0-不可删除 1-可以删除</li>
 * <li>reportAttribute: 工作汇报属性 例如：比如: DEFAULT-系统默认 参考{@link com.everhomes.rest.workReport.WorkReportAttribute}</li>
 * <li>rxMsgType: 接收提醒类型 0-即时 1-汇总</li>
 * <li>rxMsgSetting: 接收时间设置 参考{@link com.everhomes.rest.workReport.ReportMsgSettingDTO}</li>
 * <li>auMsgType: 提交提醒类型 0-不提醒 1-提醒</li>
 * <li>auMsgSetting: 提交时间设置 参考{@link com.everhomes.rest.workReport.ReportMsgSettingDTO}</li>
 * <li>updateInfo: 最后编辑信息</li>
 * <li>iconUri: 图标uri</li>
 * <li>iconUrl: 图标url</li>
 * </ul>
 */
public class WorkReportDTO {

    //  common
    private Long organizationId;

    private Long moduleId;

    private String moduleType;

    private Long reportId;

    private String reportName;

    private Byte reportType;

    private Long formOriginId;

    private Long formVersion;

    private ReportValiditySettingDTO validitySetting;

    //  web
    private Byte status;

    @ItemType(WorkReportScopeMapDTO.class)
    private List<WorkReportScopeMapDTO> scopes;

    private Byte modifyFlag;

    private Byte deleteFlag;

    private String reportAttribute;

    private Byte rxMsgType;

    private ReportMsgSettingDTO rxMsgSetting;

    private Byte auMsgType;

    private ReportMsgSettingDTO auMsgSetting;

    private Timestamp updateTime;

    private String updateInfo;

    //  app
    private String iconUri;

    private String iconUrl;

    public WorkReportDTO() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
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

    public ReportValiditySettingDTO getValiditySetting() {
        return validitySetting;
    }

    public void setValiditySetting(ReportValiditySettingDTO validitySetting) {
        this.validitySetting = validitySetting;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<WorkReportScopeMapDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<WorkReportScopeMapDTO> scopes) {
        this.scopes = scopes;
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

    public String getReportAttribute() {
        return reportAttribute;
    }

    public void setReportAttribute(String reportAttribute) {
        this.reportAttribute = reportAttribute;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
