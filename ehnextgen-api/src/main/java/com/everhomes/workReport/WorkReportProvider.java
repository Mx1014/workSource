package com.everhomes.workReport;


import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;

public interface WorkReportProvider {

    Long createWorkReport(WorkReport report);

    void updateWorkReport(WorkReport report);

    WorkReport getWorkReportById(Long id);

    WorkReport getRunningWorkReportById(Long id);

    WorkReport getWorkReportByTemplateId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long templateId);

    List<WorkReport> listWorkReports(Long pageAnchor, Integer pageSize, Long organizationId, String ownerType, Long moduleId, Byte status);

    List<WorkReport> queryWorkReports(ListingLocator locator, ListingQueryBuilderCallback queryBuilderCallback);

    void disableWorkReportByFormOriginId(Long formOriginId, Long moduleId, String moduleType);

    void createWorkReportScopeMap(WorkReportScopeMap scopeMap);

    void deleteOddWorkReportScope(Integer namespaceId, Long reportId, String sourceType, List<Long> sourceIds);

    WorkReportScopeMap getWorkReportScopeMapById(Long id);

    WorkReportScopeMap getWorkReportScopeMapBySourceId(Long reportId, Long sourceId);

    void updateWorkReportScopeMap(WorkReportScopeMap scopeMap);

    List<WorkReportScopeMap> listWorkReportScopesMap(Long reportId);

    List<WorkReportTemplate> listWorkReportTemplates(Long moduleId);

    Long createWorkReportScopeMsg(WorkReportScopeMsg msg);

    void deleteWorkReportScopeMsg();

    void deleteWorkReportScopeMsgByReportId(Long reportId);

    void updateWorkReportScopeMsg(WorkReportScopeMsg msg);

    List<WorkReportScopeMsg> listWorkReportScopeMsgByTime(java.sql.Timestamp startTime, java.sql.Timestamp endTime);
}
