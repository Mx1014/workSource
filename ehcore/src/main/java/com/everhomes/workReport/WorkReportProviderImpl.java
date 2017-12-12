package com.everhomes.workReport;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.workReport.WorkReportStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWorkReportScopeMapDao;
import com.everhomes.server.schema.tables.daos.EhWorkReportValsDao;
import com.everhomes.server.schema.tables.daos.EhWorkReportsDao;
import com.everhomes.server.schema.tables.pojos.EhWorkReportScopeMap;
import com.everhomes.server.schema.tables.pojos.EhWorkReportVals;
import com.everhomes.server.schema.tables.pojos.EhWorkReports;
import com.everhomes.server.schema.tables.records.EhWorkReportScopeMapRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportsRecord;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkReportProviderImpl implements WorkReportProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createWorkReport(WorkReport report) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReports.class));
        report.setId(id);
        report.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        report.setUpdateTime(report.getCreateTime());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        dao.insert(report);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReports.class, null);
        return report.getId();
    }

    @Override
    public void updateWorkReport(WorkReport report) {
        report.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        dao.update(report);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReports.class, report.getId());
    }

    @Override
    public WorkReport getWorkReportById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReport.class);
    }

    @Override
    public WorkReport getWorkReportByTemplateId(
            Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long templateId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportsRecord> query = context.selectQuery(Tables.EH_WORK_REPORTS);
        query.addConditions(Tables.EH_WORK_REPORTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORTS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_WORK_REPORTS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WORK_REPORTS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WORK_REPORTS.REPORT_TEMPLATE_ID.eq(templateId));
        query.addConditions(Tables.EH_WORK_REPORTS.STATUS.ne(WorkReportStatus.INVALID.getCode()));
        return query.fetchOneInto(WorkReport.class);
    }

    @Override
    public List<WorkReport> listWorkReports(
            Long pageAnchor, Integer pageSize, Long organizationId, String ownerType,
            Long moduleId, Byte status) {
        List<WorkReport> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportsRecord> query = context.selectQuery(Tables.EH_WORK_REPORTS);
        query.addConditions(Tables.EH_WORK_REPORTS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(Tables.EH_WORK_REPORTS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_WORK_REPORTS.OWNER_ID.eq(organizationId));
        query.addConditions(Tables.EH_WORK_REPORTS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WORK_REPORTS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_WORK_REPORTS.STATUS.ne(WorkReportStatus.INVALID.getCode()));
        //  little condition
        if (status != null)
            query.addConditions(Tables.EH_WORK_REPORTS.STATUS.eq(status));
        //  find by the pageAnchor
        if (pageAnchor != null)
            query.addConditions(Tables.EH_WORK_REPORTS.ID.lt(pageAnchor));
        query.addLimit(pageSize + 1);
        query.addOrderBy(Tables.EH_WORK_REPORTS.ID.asc());

        //  return back results
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReport.class));
            return null;
        });
        return results;
    }

    @Caching(evict = {@CacheEvict(value = "listWorkReportScopesMap", key = "#scopeMap.reportId")})
    @Override
    public void createWorkReportScopeMap(WorkReportScopeMap scopeMap) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportScopeMap.class));
        scopeMap.setId(id);
        scopeMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportScopeMapDao dao = new EhWorkReportScopeMapDao(context.configuration());
        dao.insert(scopeMap);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportScopeMap.class, null);
    }

    @Caching(evict = {@CacheEvict(value = "listWorkReportScopesMap", key = "#reportId")})
    @Override
    public void deleteWorkReportScopeMapNotInIds(Long reportId, List<Long> sourceIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportScopeMapRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_SCOPE_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.REPORT_ID.eq(reportId));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.SOURCE_ID.notIn(sourceIds));
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportScopeMap.class, null);
    }

    @Override
    public WorkReportScopeMap getWorkReportScopeMapById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportScopeMapDao dao = new EhWorkReportScopeMapDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReportScopeMap.class);
    }

    @Override
    public WorkReportScopeMap getWorkReportScopeMapBySourceId(Long reportId, Long sourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportScopeMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_SCOPE_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.REPORT_ID.eq(reportId));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.SOURCE_ID.eq(sourceId));
        return query.fetchOneInto(WorkReportScopeMap.class);
    }

    @Caching(evict = {@CacheEvict(value = "listWorkReportScopesMap", key = "#scopeMap.reportId")})
    @Override
    public void updateWorkReportScopeMap(WorkReportScopeMap scopeMap) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportScopeMapDao dao = new EhWorkReportScopeMapDao(context.configuration());
        dao.update(scopeMap);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportScopeMap.class, scopeMap.getId());
    }

    @Cacheable(value = "listWorkReportScopesMap", key = "#reportId", unless = "#result.size() == 0")
    @Override
    public List<WorkReportScopeMap> listWorkReportScopesMap(Long reportId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportScopeMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_SCOPE_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.REPORT_ID.eq(reportId));
        query.addOrderBy(Tables.EH_WORK_REPORT_SCOPE_MAP.CREATE_TIME.asc());
        List<WorkReportScopeMap> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportScopeMap.class));
            return null;
        });
        return results;
    }

    @Override
    public List<WorkReportTemplate> listWorkReportTemplates(Long moduleId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportTemplatesRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_TEMPLATES);
        query.addConditions(Tables.EH_WORK_REPORT_TEMPLATES.MODULE_ID.eq(moduleId));
        List<WorkReportTemplate> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportTemplate.class));
            return null;
        });
        if (null != results && results.size() > 0)
            return results;
        return null;
    }
}
