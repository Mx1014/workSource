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
import com.everhomes.server.schema.tables.records.*;
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
    public WorkReportVal getWorkReportValById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReportVal.class);
    }

    @Override
    public List<WorkReportVal> listWorkReportValsByUserIds(
            Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, List<Long> applierIds) {
        List<WorkReportVal> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VALS);
        query.addConditions(Tables.EH_WORK_REPORT_VALS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.STATUS.eq(WorkReportStatus.VALID.getCode()));
        if (applierIds != null && applierIds.size() > 0)
            query.addConditions(Tables.EH_WORK_REPORT_VALS.APPLIER_USER_ID.in(applierIds));
        //  set the pageOffset condition
        query.addLimit(pageOffset, pageSize + 1);
        query.addOrderBy(Tables.EH_WORK_REPORT_VALS.UPDATE_TIME.desc());

        //  return back
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportVal.class));
            return null;
        });
        return results;
    }

    @Caching(evict = {@CacheEvict(value = "listReportValReceiversByValId", key = "#receiver.reportValId")})
    @Override
    public void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportValReceiverMap.class));
        receiver.setId(id);
        receiver.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValReceiverMapDao dao = new EhWorkReportValReceiverMapDao(context.configuration());
        dao.insert(receiver);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportValReceiverMap.class, null);
    }

    @Caching(evict = {@CacheEvict(value = "listReportValReceiversByValId", key = "#reportValId")})
    @Override
    public void deleteReportValReceiverByValId(Long reportValId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportValReceiverMapRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(reportValId));
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportScopeMap.class, null);
    }

    @Cacheable(value = "listReportValReceiversByValId", key = "#reportValId", unless = "#result.size() == 0")
    @Override
    public List<WorkReportValReceiverMap> listReportValReceiversByValId(Long reportValId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(reportValId));
        List<WorkReportValReceiverMap> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportValReceiverMap.class));
            return null;
        });
        return results;
    }
}
