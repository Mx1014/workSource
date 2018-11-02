package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyTaskStatus;
import com.everhomes.rest.energy.TaskGeneratePaymentFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterTasksDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterTasks;
import com.everhomes.server.schema.tables.records.EhEnergyMeterTasksRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/23.
 */
@Component
public class EnergyMeterTaskProviderImpl implements EnergyMeterTaskProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyMeterTaskProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
    public void createEnergyMeterTask(EnergyMeterTask task) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterTasks.class));

        task.setId(id);
        task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        task.setStatus(EnergyTaskStatus.NON_READ.getCode());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyMeterTasks.class));
        EhEnergyMeterTasksDao dao = new EhEnergyMeterTasksDao(context.configuration());
        dao.insert(task);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnergyMeterTasks.class, id);
    }

    @Override
    public void updateEnergyMeterTask(EnergyMeterTask task) {
        assert(task.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyMeterTasks.class));
        EhEnergyMeterTasksDao dao = new EhEnergyMeterTasksDao(context.configuration());
        task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        task.setOperatorUid(UserContext.currentUserId());
        dao.update(task);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnergyMeterTasks.class, task.getId());
    }

    @Override
    public EnergyMeterTask findEnergyMeterTaskById(Long taskId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyMeterTasks.class));
        EhEnergyMeterTasksDao dao = new EhEnergyMeterTasksDao(context.configuration());
        return ConvertHelper.convert(dao.findById(taskId), EnergyMeterTask.class);
    }

    @Override
    public EnergyMeterTask findEnergyMeterTaskByMeterId(Long meterId, Timestamp now) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_TASKS)
                .where(Tables.EH_ENERGY_METER_TASKS.METER_ID.eq(meterId))
                .and(Tables.EH_ENERGY_METER_TASKS.STATUS.ne(EnergyTaskStatus.INACTIVE.getCode()))
                .and(Tables.EH_ENERGY_METER_TASKS.EXECUTIVE_START_TIME.le(now))
                .and(Tables.EH_ENERGY_METER_TASKS.EXECUTIVE_EXPIRE_TIME.ge(now))
                .fetchAnyInto(EnergyMeterTask.class);
    }

    @Override
    public List<EnergyMeterTask> listEnergyMeterTasks(long pageAnchor, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_TASKS)
                .where(Tables.EH_ENERGY_METER_TASKS.ID.ge(pageAnchor))
                .and(Tables.EH_ENERGY_METER_TASKS.STATUS.ne(EnergyTaskStatus.INACTIVE.getCode()))
                .orderBy(Tables.EH_ENERGY_METER_TASKS.ID)
                .limit(pageSize).fetchInto(EnergyMeterTask.class);
    }

    @Override
    public List<EnergyMeterTask> listNotGeneratePaymentEnergyMeterTasks() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_TASKS)
                .where(Tables.EH_ENERGY_METER_TASKS.EXECUTIVE_EXPIRE_TIME.le(new Timestamp(DateHelper.currentGMTTime().getTime())))
                .and(Tables.EH_ENERGY_METER_TASKS.STATUS.in(EnergyTaskStatus.NON_READ.getCode(), EnergyTaskStatus.READ.getCode()))
                .and(Tables.EH_ENERGY_METER_TASKS.GENERATE_PAYMENT_FLAG.eq(TaskGeneratePaymentFlag.NON_GENERATE.getCode()))
                .fetchInto(EnergyMeterTask.class);
    }

    @Override
    public List<EnergyMeterTask> listEnergyMeterTasksByPlan(List<Long> planIds, Long targetId, Long ownerId, long pageAnchor, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_TASKS)
                .where(Tables.EH_ENERGY_METER_TASKS.ID.ge(pageAnchor))
                .and(Tables.EH_ENERGY_METER_TASKS.PLAN_ID.in(planIds))
                .and(Tables.EH_ENERGY_METER_TASKS.STATUS.ne(EnergyTaskStatus.INACTIVE.getCode()))
//                .and(Tables.EH_ENERGY_METER_TASKS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_ENERGY_METER_TASKS.TARGET_ID.eq(targetId))
                .orderBy(Tables.EH_ENERGY_METER_TASKS.PLAN_ID, Tables.EH_ENERGY_METER_TASKS.DEFAULT_ORDER)
                .limit(pageSize).fetchInto(EnergyMeterTask.class);
    }

    @Override
    public List<EnergyMeterTask> listEnergyMeterTasksByPlan(List<Long> planIds, Long targetId, Long ownerId, long pageAnchor, int pageSize, Timestamp lastUpdateTime) {
        List<EnergyMeterTask> tasks = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhEnergyMeterTasksRecord> query = context.selectQuery(Tables.EH_ENERGY_METER_TASKS);
        Condition condition = Tables.EH_ENERGY_METER_TASKS.PLAN_ID.in(planIds);
        condition = condition.and(Tables.EH_ENERGY_METER_TASKS.STATUS.in(EnergyTaskStatus.READ.getCode(), EnergyTaskStatus.NON_READ.getCode()));
        condition = condition.and(Tables.EH_ENERGY_METER_TASKS.TARGET_ID.eq(targetId)).and(Tables.EH_ENERGY_METER_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));
        if (lastUpdateTime != null) {
            condition.and(Tables.EH_ENERGY_METER_TASKS.CREATE_TIME.gt(lastUpdateTime)
                    .or(Tables.EH_ENERGY_METER_TASKS.UPDATE_TIME.gt(lastUpdateTime)));
        }
//        // add for auto reading meters
//        condition = condition.or(Tables.EH_ENERGY_METER_TASKS.PLAN_ID.eq(0L));

        query.addConditions(condition);
        query.addOrderBy(Tables.EH_ENERGY_METER_TASKS.PLAN_ID, Tables.EH_ENERGY_METER_TASKS.DEFAULT_ORDER);
        query.addConditions(Tables.EH_ENERGY_METER_TASKS.ID.ge(pageAnchor));
        query.addLimit(pageSize);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listEnergyMeterTasksByPlan, sql=" + query.getSQL());
            LOGGER.debug("listEnergyMeterTasksByPlan, bindValues=" + query.getBindValues());
        }
        query.fetch().map((r) -> {
            tasks.add(ConvertHelper.convert(r, EnergyMeterTask.class));
            return null;
        });

        return tasks;
    }

    @Override
    public Map<Long, EnergyMeterTask> listEnergyMeterTasks(List<Long> ids) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnergyMeterTasksRecord> query = context.selectQuery(Tables.EH_ENERGY_METER_TASKS);
        query.addConditions(Tables.EH_ENERGY_METER_TASKS.ID.in(ids));
        query.addConditions(Tables.EH_ENERGY_METER_TASKS.STATUS.ne(EnergyTaskStatus.INACTIVE.getCode()));

        Map<Long, EnergyMeterTask> result = new HashMap<>();
        query.fetch().map((r) -> {
            result.put(r.getId(), ConvertHelper.convert(r, EnergyMeterTask.class));
            return null;
        });

        return result;
    }

    @Override
    public List<EnergyMeterTask> listActiveEnergyMeterTasks(Long meterId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_TASKS)
                .where(Tables.EH_ENERGY_METER_TASKS.METER_ID.eq(meterId))
                .and(Tables.EH_ENERGY_METER_TASKS.STATUS.ne(EnergyTaskStatus.INACTIVE.getCode()))
                .orderBy(Tables.EH_ENERGY_METER_TASKS.CREATE_TIME.desc())
                .fetchInto(EnergyMeterTask.class);

    }
}
