package com.everhomes.workReport;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.workReport.WorkReportReadStatus;
import com.everhomes.rest.workReport.WorkReportStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWorkReportValReceiverMapDao;
import com.everhomes.server.schema.tables.daos.EhWorkReportValsDao;
import com.everhomes.server.schema.tables.pojos.EhWorkReportScopeMap;
import com.everhomes.server.schema.tables.pojos.EhWorkReportValReceiverMap;
import com.everhomes.server.schema.tables.pojos.EhWorkReportVals;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
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
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportVals.class, val.getId());

    }

    @Override
    public WorkReportVal getWorkReportValById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReportVal.class);
    }

    @Override
    public List<WorkReportVal> listWorkReportValsByApplierIds(
            Integer namespaceId, Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, List<Long> applierIds) {
        List<WorkReportVal> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VALS);
        query.addConditions(Tables.EH_WORK_REPORT_VALS.NAMESPACE_ID.eq(namespaceId));
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

    @Override
    public List<WorkReportVal> listWorkReportValsByReceiverId(
            Integer namespaceId, Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, Long receiverId, Byte readStatus) {
        List<WorkReportVal> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addJoin(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP, JoinType.JOIN,
                Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(Tables.EH_WORK_REPORT_VALS.ID));
/*        query.addSelect(Tables.EH_WORK_REPORT_VALS.ID.as("reportValId"));
        query.addSelect(Tables.EH_WORK_REPORT_VALS.REPORT_ID.as("reportId"));
        query.addSelect(Tables.EH_WORK_REPORT_VALS.REPORT_TIME);
        query.addSelect(Tables.EH_WORK_REPORT_VALS.UPDATE_TIME);
        query.addSelect(Tables.EH_WORK_REPORT_VALS.APPLIER_NAME);*/
//        query.addSelect(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.RECEIVER_USER_ID.eq(receiverId));
        if (readStatus != null)
            query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS.eq(readStatus));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_TYPE.eq(ownerType));

        //  set the pageOffset condition
        query.addLimit(pageOffset, pageSize + 1);
        query.addOrderBy(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.CREATE_TIME.desc());

        //  return back
        query.fetch().map(r ->{
            WorkReportVal reportVal = new WorkReportVal();
            reportVal.setId(r.getValue(Tables.EH_WORK_REPORT_VALS.ID));
            reportVal.setReportId(r.getValue(Tables.EH_WORK_REPORT_VALS.REPORT_ID));
            reportVal.setReportTime(r.getValue(Tables.EH_WORK_REPORT_VALS.REPORT_TIME));
            reportVal.setApplierName(r.getValue(Tables.EH_WORK_REPORT_VALS.APPLIER_NAME));
            reportVal.setReadStatus(r.getValue(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS));
            reportVal.setUpdateTime(r.getValue(Tables.EH_WORK_REPORT_VALS.UPDATE_TIME));
            results.add(reportVal);
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
        receiver.setUpdateTime(receiver.getCreateTime());
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
        query.execute();
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

    @Override
    public Integer countUnReadWorkReportsVal(Integer namespaceId, Long receiverId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.RECEIVER_USER_ID.eq(receiverId));
        return query.fetchCount();
    }

    @Override
    public void markWorkReportsValReading(Integer namespaceId, Long receiverId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhWorkReportValReceiverMapRecord> query = context.updateQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addValue(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS, WorkReportReadStatus.READ.getCode());
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.RECEIVER_USER_ID.eq(receiverId));
        query.execute();
    }
}
