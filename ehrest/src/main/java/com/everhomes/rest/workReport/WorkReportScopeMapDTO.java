package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * <li>sourceType: 选择对象的类型 例：ORGANIZATION, MEMBERDETAIL, user 参考{@link com.everhomes.rest.uniongroup.UniongroupTargetType}</li>
 * <li>sourceId: 选择对象的id</li>
 * <li>sourceDescription: 选择对象的名称</li>
 * </ul>
 */
public class WorkReportScopeMapDTO {

    private Long id;

    private Long reportId;

    private String sourceType;

    private Long sourceId;

    private String sourceDescription;

    public WorkReportScopeMapDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
