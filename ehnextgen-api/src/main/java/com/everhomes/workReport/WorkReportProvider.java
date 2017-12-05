package com.everhomes.workReport;

import com.everhomes.rest.workReport.WorkReportDTO;

import java.util.List;

public interface WorkReportProvider {

    void createWorkReport(WorkReport report);

    void updateWorkReport(WorkReport report);

    WorkReport findWorkReport(Long id);

    List<WorkReport> listWorkReports(Long pageAnchor, Integer count, Long organizationId, String ownerType, Long moduleId, Byte status);
}
