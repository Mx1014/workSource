// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.filedownload.AllReadStatus;
import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhTasksDao;
import com.everhomes.server.schema.tables.pojos.EhTasks;
import com.everhomes.server.schema.tables.records.EhTasksRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class TaskProviderImpl implements TaskProvider {

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createTask(Task job) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTasks.class));
        job.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhTasksDao dao = new EhTasksDao(context.configuration());
        dao.insert(job);
    }

    @Override
    public void updateTask(Task job) {
        assert(job.getId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhTasksDao dao = new EhTasksDao(context.configuration());
        dao.update(job);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhTasks.class, job.getId());
    }


    @Override
    public Task findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhTasks.class, id));
        EhTasksDao dao = new EhTasksDao(context.configuration());
        EhTasks result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, Task.class);
    }

    @Override
    public List<Task> listTask(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, String keyword, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTasksRecord> query = context.selectQuery(Tables.EH_TASKS);

        if(namespaceId != null){
            query.addConditions(Tables.EH_TASKS.NAMESPACE_ID.eq(namespaceId));
        }

        if(communityId != null){
            query.addConditions(Tables.EH_TASKS.COMMUNITY_ID.eq(communityId));
        }

        if(orgId != null){
            query.addConditions(Tables.EH_TASKS.ORG_ID.eq(orgId));
        }

        if(userId != null){
            query.addConditions(Tables.EH_TASKS.USER_ID.eq(userId));
        }

        if(type != null){
            query.addConditions(Tables.EH_TASKS.TYPE.eq(type));
        }

        if(status != null){
            query.addConditions(Tables.EH_TASKS.STATUS.eq(status));
        }

        if(startTime != null){
            query.addConditions(Tables.EH_TASKS.CREATE_TIME.ge(new Timestamp(startTime)));
        }

        if(endTime != null){
            query.addConditions(Tables.EH_TASKS.CREATE_TIME.le(new Timestamp(endTime)));
        }

        if(keyword != null){
            query.addConditions(Tables.EH_TASKS.NAME.like("%" + keyword + "%"));
        }

        if(pageAnchor != null){
            query.addConditions(Tables.EH_TASKS.ID.le(pageAnchor));
        }

        query.addOrderBy(Tables.EH_TASKS.ID.desc());

        if(pageSize != null){
            query.addLimit(pageSize);
        }


        List<Task> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Task.class));
            return null;
        });

        return result;
    }

    @Override
    public List<Task> listWaitingAndRunningTask() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTasksRecord> query = context.selectQuery(Tables.EH_TASKS);
        query.addConditions(Tables.EH_TASKS.STATUS.in(TaskStatus.RUNNING.getCode(), TaskStatus.WAITING.getCode()));
        query.addOrderBy(Tables.EH_TASKS.ID.asc());
        List<Task> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Task.class));
            return null;
        });

        return result;
    }

    @Override
    public List<Long> listWaitingTaskIds() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Long> result = context.select(Tables.EH_TASKS.ID).from(Tables.EH_TASKS)
                .where(Tables.EH_TASKS.STATUS.eq(TaskStatus.WAITING.getCode()))
                .orderBy(Tables.EH_TASKS.ID.asc())
                .fetchInto(Long.class);
        if(result == null){
            result = new ArrayList<>();
        }

        return result;
    }

    @Override
    public Integer countNotAllReadStatus(Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Integer count = context.select(DSL.count(Tables.EH_TASKS.ID))
                .from(Tables.EH_TASKS)
                .where(Tables.EH_TASKS.READ_STATUS.eq(AllReadStatus.NOTALLREAD.getCode()))
                .and(Tables.EH_TASKS.USER_ID.eq(userId))
                .fetchOne(DSL.count(Tables.EH_TASKS.ID));
        return count;
    }

    @Override
    public List<Task> listNotReadStatusTasks(Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhTasksRecord> query = context.selectQuery(Tables.EH_TASKS);
        query.addConditions(Tables.EH_TASKS.READ_STATUS.eq(AllReadStatus.NOTALLREAD.getCode())
                .and(Tables.EH_TASKS.USER_ID.eq(userId)));
        List<Task> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Task.class));
            return null;
        });

        return result;
    }
}
