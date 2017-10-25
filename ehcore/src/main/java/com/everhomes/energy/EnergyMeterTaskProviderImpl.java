package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    }

    @Override
    public void updateEnergyMeterTask(EnergyMeterTask task) {

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
