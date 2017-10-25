package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterTasksDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterTasks;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/23.
 */
@Component
public class EnergyMeterTaskProviderImpl implements EnergyMeterTaskProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
    public void createEnergyMeterTask(EnergyMeterTask task) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterTasks.class));

        task.setId(id);
        task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

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
        return null;
    }

    @Override
    public List<EnergyMeterTask> listEnergyMeterTasks(long pageAnchor, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_METER_TASKS)
                .where(Tables.EH_ENERGY_METER_TASKS.ID.ge(pageAnchor))
                .limit(pageSize).fetchInto(EnergyMeterTask.class);
    }

    @Override
    public Map<Long, EnergyMeterTask> listEnergyMeterTasks(List<Long> ids) {
        return null;
    }
}
