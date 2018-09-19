package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>reportId: 工作汇报id</li>
 * </ul>
 */
public class WorkReportIdCommand {

    private Integer namespaceId;

    private Long reportId;

    public WorkReportIdCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
