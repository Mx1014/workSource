package com.everhomes.workReport;

import com.everhomes.rest.workReport.WorkReportDTO;

import java.util.List;

public interface WorkReportProvider {

    void createWorkReport(WorkReport report);

    void updateWorkReport(WorkReport report);

    WorkReport getWorkReportById(Long id);

    WorkReport getWorkReportByTemplateId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long templateId);

    List<WorkReport> listWorkReports(Long pageAnchor, Integer count, Long organizationId, String ownerType, Long moduleId, Byte status);

    void createWorkReportScopeMap(WorkReportScopeMap scopeMap);

    void deleteWorkReportScopeMapNotInIds(Long reportId, List<Long> sourceIds);

    WorkReportScopeMap getWorkReportScopeMapById(Long id);

    WorkReportScopeMap getWorkReportScopeMapBySourceId(Long reportId, Long sourceId);

    void updateWorkReportScopeMap(WorkReportScopeMap scopeMap);

    List<WorkReportScopeMap> listWorkReportScopeMap(Long reportId);

    List<WorkReportTemplate> listWorkReportTemplates(Long moduleId);

}
