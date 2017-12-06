package com.everhomes.rest.workReport;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>reportId: 工作汇报id</li>
 * <li>reportName: 工作汇报名称</li>
 * <li>status: 汇报状态 0-无效 1-未启用 2-启用 参考{@link com.everhomes.rest.workReport.WorkReportStatus}</li>
 * <li>scopes: 可见范围 参考{@link com.everhomes.rest.workReport.WorkReportScopeMapDTO}</li>
 * <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改 {@link com.everhomes.rest.workReport.AttitudeFlag}</li>
 * <li>deleteFlag: 是否可删除 0-不可删除 1-可以删除</li>
 * <li>reportType: 工作汇报类型 0-每日 1-每周 2-每月 参考{@link com.everhomes.rest.workReport.WorkReportType}</li>
 * <li>reportAttribute: 工作汇报属性 例如：比如: DEFAULT-系统默认 参考{@link com.everhomes.rest.workReport.WorkReportAttribute}</li>
 * <li>formOriginId: 关联表单id</li>
 * <li>formVersion: 关联表单版本</li>
 * <li>updateInfo: 最后编辑信息</li>
 * </ul>
 */
public class WorkReportDTO {

    private Long organizationId;

    private Long moduleId;

    private String moduleType;

    private Long reportId;

    private String reportName;

    private Byte status;

    @ItemType(WorkReportScopeMapDTO.class)
    private List<WorkReportScopeMapDTO> scopes;

    private Byte modifyFlag;

    private Byte deleteFlag;

    private Byte reportType;

    private String reportAttribute;

    private Long formOriginId;

    private Long formVersion;

    private Timestamp updateTime;

    private String updateInfo;

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

    public Byte getReportType() {
        return reportType;
    }

    public void setReportType(Byte reportType) {
        this.reportType = reportType;
    }

    public String getReportAttribute() {
        return reportAttribute;
    }

    public void setReportAttribute(String reportAttribute) {
        this.reportAttribute = reportAttribute;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
