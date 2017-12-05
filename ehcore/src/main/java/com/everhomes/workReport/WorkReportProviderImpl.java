package com.everhomes.workReport;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.workReport.WorkReportStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWorkReportsDao;
import com.everhomes.server.schema.tables.pojos.EhWorkReports;
import com.everhomes.server.schema.tables.records.EhWorkReportsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createWorkReport(WorkReport report) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReports.class));
        report.setId(id);
        report.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        report.setUpdateTime(report.getCreateTime());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        dao.insert(report);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReports.class, null);
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
    public WorkReport findWorkReport(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReport.class);
    }

    @Override
    public List<WorkReport> listWorkReports(
            Long pageAnchor, Integer count, Long organizationId, String ownerType,
            Long moduleId, Byte status) {
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
        query.addLimit(count + 1);
        query.addOrderBy(Tables.EH_WORK_REPORTS.ID.asc());

        //  return back results
        List<WorkReport> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReport.class));
            return null;
        });
        if (null != results && 0 < results.size()) {
            return results;
        }
        return null;
    }
}
