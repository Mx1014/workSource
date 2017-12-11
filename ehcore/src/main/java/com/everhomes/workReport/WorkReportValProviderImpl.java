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
import com.everhomes.server.schema.tables.daos.EhWorkReportValReceiverMapDao;
import com.everhomes.server.schema.tables.daos.EhWorkReportValsDao;
import com.everhomes.server.schema.tables.daos.EhWorkReportsDao;
import com.everhomes.server.schema.tables.pojos.EhWorkReportScopeMap;
import com.everhomes.server.schema.tables.pojos.EhWorkReportValReceiverMap;
import com.everhomes.server.schema.tables.pojos.EhWorkReportVals;
import com.everhomes.server.schema.tables.pojos.EhWorkReports;
import com.everhomes.server.schema.tables.records.EhWorkReportScopeMapRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportValReceiverMapRecord;
import com.everhomes.server.schema.tables.records.EhWorkReportsRecord;
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
public class WorkReportValProviderImpl implements WorkReportValProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createWorkReportVal(WorkReportVal val) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportVals.class));
        val.setId(id);
        val.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        val.setUpdateTime(val.getCreateTime());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        dao.insert(val);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportVals.class, null);
        return val.getId();
    }

    @Override
    public void updateWorkReportVal(WorkReportVal val) {
        val.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        dao.update(val);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportVals.class, val.getId());

    }

    @Override
    public WorkReportVal getWorkReportValById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReportVal.class);
    }

    @Override
    public void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportValReceiverMap.class));
        receiver.setId(id);
        receiver.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValReceiverMapDao dao = new EhWorkReportValReceiverMapDao(context.configuration());
        dao.insert(receiver);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportValReceiverMap.class, null);
    }

    @Override
    public void deleteReportValReceiverByValId(Long valId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportValReceiverMapRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(valId));
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportScopeMap.class, null);
    }
}
