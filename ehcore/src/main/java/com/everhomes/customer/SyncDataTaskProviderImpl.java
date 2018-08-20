package com.everhomes.customer;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.customer.SyncDataTaskStatus;
import com.everhomes.rest.customer.SyncResultViewedFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSyncDataErrorsDao;
import com.everhomes.server.schema.tables.daos.EhSyncDataTasksDao;
import com.everhomes.server.schema.tables.pojos.EhSyncDataErrors;
import com.everhomes.server.schema.tables.pojos.EhSyncDataTasks;
import com.everhomes.server.schema.tables.records.EhSyncDataErrorsRecord;
import com.everhomes.server.schema.tables.records.EhSyncDataTasksRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2018/1/13.
 */
@Component
public class SyncDataTaskProviderImpl implements SyncDataTaskProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSyncDataTask(SyncDataTask task) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSyncDataTasks.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncDataTasks.class));
        EhSyncDataTasksDao dao = new EhSyncDataTasksDao(context.configuration());
        task.setId(id);
        task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(task);
    }

    @Override
    public void updateSyncDataTask(SyncDataTask task) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncDataTasks.class));
        EhSyncDataTasksDao dao = new EhSyncDataTasksDao(context.configuration());
        task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(task);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSyncDataTasks.class, task.getId());
    }

    @Override
    public void updateSyncDataTask(SyncDataTask task, boolean doUpdateTime) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncDataTasks.class));
        EhSyncDataTasksDao dao = new EhSyncDataTasksDao(context.configuration());
        if(doUpdateTime){
            task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        }
        dao.update(task);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSyncDataTasks.class, task.getId());
    }

    @Override
    public SyncDataTask findSyncDataTaskById(Long taskId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhSyncDataTasksDao dao = new EhSyncDataTasksDao(context.configuration());
        return ConvertHelper.convert(dao.findById(taskId), SyncDataTask.class);
    }

    @Override
    public SyncDataTask findExecutingSyncDataTask(Long communityId, String syncType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhSyncDataTasksRecord> query = context.selectQuery(Tables.EH_SYNC_DATA_TASKS);
        query.addConditions(Tables.EH_SYNC_DATA_TASKS.OWNER_ID.eq(communityId));
        query.addConditions(Tables.EH_SYNC_DATA_TASKS.TYPE.eq(syncType));
        query.addConditions(Tables.EH_SYNC_DATA_TASKS.STATUS.eq(SyncDataTaskStatus.EXECUTING.getCode()));

        return query.fetchAnyInto(SyncDataTask.class);
    }

    @Override
    public List<SyncDataTask> listCommunitySyncResult(Long communityId, String syncType, Integer pageSize, Long pageAnchor) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhSyncDataTasksRecord> query = context.selectQuery(Tables.EH_SYNC_DATA_TASKS);
        query.addConditions(Tables.EH_SYNC_DATA_TASKS.OWNER_ID.eq(communityId));
        query.addConditions(Tables.EH_SYNC_DATA_TASKS.TYPE.eq(syncType));

        if(pageAnchor != null) {
            query.addConditions(Tables.EH_SYNC_DATA_TASKS.ID.le(pageAnchor));
        }

        query.addOrderBy(Tables.EH_SYNC_DATA_TASKS.ID.desc());
        query.addLimit(pageSize);

        List<SyncDataTask> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, SyncDataTask.class));
            return null;
        });

        return result;
    }

    @Override
    public Integer countNotViewedSyncResult(Long communityId, String syncType) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).selectCount().from(Tables.EH_SYNC_DATA_TASKS)
                .where(Tables.EH_SYNC_DATA_TASKS.OWNER_ID.eq(communityId))
                .and(Tables.EH_SYNC_DATA_TASKS.TYPE.eq(syncType))
                .and(Tables.EH_SYNC_DATA_TASKS.STATUS.eq(SyncDataTaskStatus.EXCEPTION.getCode()))
                .and(Tables.EH_SYNC_DATA_TASKS.VIEW_FLAG.eq(SyncResultViewedFlag.NOT_VIEWED.getCode()))
                .fetchAny().value1();
    }

    @Override
    public Long createSyncErrorMsg(Integer namespaceId,  String syncType, Long ownerId, String ownerType, String errorMessage, Long taskId){
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSyncDataErrors.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncDataTasks.class));
        EhSyncDataErrorsDao dao = new EhSyncDataErrorsDao(context.configuration());
        EhSyncDataErrors err = new EhSyncDataErrors();
        err.setId(id);
        err.setErrorMessage(errorMessage);
        err.setNamespaceId(namespaceId);
        err.setSyncType(syncType);
        err.setOwnerId(ownerId);
        err.setOwnerType(ownerType);
        err.setTaskId(taskId);
        //task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(err);
        return id;
    }


    @Override
    public List<SyncDataError> listSyncErrorMsgByTaskId(Long taskId, String syncType, Long pageAnchor, Integer pageSize){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhSyncDataErrorsRecord> query = context.selectQuery(Tables.EH_SYNC_DATA_ERRORS);
        query.addConditions(Tables.EH_SYNC_DATA_ERRORS.TASK_ID.eq(taskId));
        query.addConditions(Tables.EH_SYNC_DATA_ERRORS.SYNC_TYPE.eq(syncType));

        if(pageAnchor != null) {
            query.addConditions(Tables.EH_SYNC_DATA_ERRORS.ID.le(pageAnchor));
        }

        query.addOrderBy(Tables.EH_SYNC_DATA_ERRORS.ID.desc());
        query.addLimit(pageSize);

        List<SyncDataError> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, SyncDataError.class));
            return null;
        });

        return result;
    }
}
