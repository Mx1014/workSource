package com.everhomes.workReport;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.workReport.ReportAuthorMsgType;
import com.everhomes.rest.workReport.WorkReportStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWorkReportScopeMapDao;
import com.everhomes.server.schema.tables.daos.EhWorkReportScopeMsgDao;
import com.everhomes.server.schema.tables.daos.EhWorkReportsDao;
import com.everhomes.server.schema.tables.pojos.EhWorkReportScopeMap;
import com.everhomes.server.schema.tables.pojos.EhWorkReportScopeMsg;
import com.everhomes.server.schema.tables.pojos.EhWorkReports;
import com.everhomes.server.schema.tables.records.EhWorkReportScopeMapRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportScopeMsgRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
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
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReports.class, report.getId());
    }

    @Override
    public WorkReport getWorkReportById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReport.class);
    }

    @Override
    public WorkReport getRunningWorkReportById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportsRecord> query = context.selectQuery(Tables.EH_WORK_REPORTS);
        query.addConditions(Tables.EH_WORK_REPORTS.ID.eq(id));
        query.addConditions(Tables.EH_WORK_REPORTS.STATUS.eq(WorkReportStatus.RUNNING.getCode()));
        return query.fetchOneInto(WorkReport.class);
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
            query.addConditions(Tables.EH_WORK_REPORTS.ID.gt(pageAnchor));
        query.addLimit(pageSize + 1);
        query.addOrderBy(Tables.EH_WORK_REPORTS.ID.asc());

        //  return back results
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReport.class));
            return null;
        });
        return results;
    }

    @Override
    public List<WorkReport> queryWorkReports(ListingLocator locator, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportsRecord> query = context.selectQuery(Tables.EH_WORK_REPORTS);
        queryBuilderCallback.buildCondition(locator, query);
        List<WorkReport> results = query.fetchInto(WorkReport.class);
        if (null == results || results.size() == 0)
            return null;
        return results;
    }

    @Override
    public void disableWorkReportByFormOriginId(Long formOriginId, Long moduleId, String moduleType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhWorkReportsRecord> query = context.updateQuery(Tables.EH_WORK_REPORTS);
        query.addValue(Tables.EH_WORK_REPORTS.FORM_ORIGIN_ID, 0L);
        query.addValue(Tables.EH_WORK_REPORTS.STATUS, WorkReportStatus.VALID.getCode());
        query.addConditions(Tables.EH_WORK_REPORTS.STATUS.ne(WorkReportStatus.INVALID.getCode()));
        query.addConditions(Tables.EH_WORK_REPORTS.FORM_ORIGIN_ID.eq(formOriginId));
        query.addConditions(Tables.EH_WORK_REPORTS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_WORK_REPORTS.MODULE_TYPE.eq(moduleType));
        query.execute();
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
    public void deleteOddWorkReportScope(Integer namespaceId, Long reportId, String sourceType, List<Long> sourceIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportScopeMapRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_SCOPE_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.REPORT_ID.eq(reportId));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.SOURCE_TYPE.eq(sourceType));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MAP.SOURCE_ID.notIn(sourceIds));
        query.execute();
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
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportScopeMap.class, scopeMap.getId());
    }

    @Cacheable(value = "listWorkReportScopesMap", key = "#reportId", unless = "#result.size() == 0")
    @Override
    public List<WorkReportScopeMap> listWorkReportScopesMap(Long reportId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportScopeMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_SCOPE_MAP);
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

    @Override
    public Long createWorkReportScopeMsg(WorkReportScopeMsg msg){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportScopeMsg.class));
        msg.setId(id);
        msg.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportScopeMsgDao dao = new EhWorkReportScopeMsgDao(context.configuration());
        dao.insert(msg);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportScopeMsg.class, null);
        return msg.getId();
    }

    @Override
    public void deleteWorkReportScopeMsg() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportScopeMsgRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_SCOPE_MSG);
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MSG.REMINDER_TIME.lt(new Timestamp(DateHelper.currentGMTTime().getTime())));
        query.execute();
    }

    @Override
    public void deleteWorkReportScopeMsgByReportId(Long reportId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportScopeMsgRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_SCOPE_MSG);
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MSG.REPORT_ID.eq(reportId));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MSG.REMINDER_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));
        query.execute();
    }

    @Override
    public void updateWorkReportScopeMsg(WorkReportScopeMsg msg){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportScopeMsgDao dao = new EhWorkReportScopeMsgDao(context.configuration());
        dao.update(msg);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportScopeMsg.class, msg.getId());
    }

    @Override
    public List<WorkReportScopeMsg> listWorkReportScopeMsgByTime(java.sql.Timestamp startTime, java.sql.Timestamp endTime){
        List<WorkReportScopeMsg> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportScopeMsgRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_SCOPE_MSG);
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MSG.REMINDER_TIME.ge(startTime));
        query.addConditions(Tables.EH_WORK_REPORT_SCOPE_MSG.REMINDER_TIME.le(endTime));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportScopeMsg.class));
            return null;
        });
        return results;
    }

}
