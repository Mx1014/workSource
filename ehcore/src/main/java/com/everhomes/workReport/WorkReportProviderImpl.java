package com.everhomes.workReport;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.workReport.WorkReportStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhWorkReportsDao;
import com.everhomes.server.schema.tables.pojos.EhWorkReports;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

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
    public void updateWorkReport(WorkReport report){
        report.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        dao.update(report);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReports.class, report.getId());
    }

    @Override
    public WorkReport findWorkReport(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportsDao dao = new EhWorkReportsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReport.class);
    }


}
