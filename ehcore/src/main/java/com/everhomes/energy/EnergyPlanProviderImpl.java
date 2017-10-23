package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyPlanGroupMapDao;
import com.everhomes.server.schema.tables.daos.EhEnergyPlanMeterMapDao;
import com.everhomes.server.schema.tables.daos.EhEnergyPlansDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyPlanGroupMap;
import com.everhomes.server.schema.tables.pojos.EhEnergyPlanMeterMap;
import com.everhomes.server.schema.tables.pojos.EhEnergyPlans;
import com.everhomes.server.schema.tables.records.EhEnergyPlanGroupMapRecord;
import com.everhomes.server.schema.tables.records.EhEnergyPlanMeterMapRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/20.
 */
public class EnergyPlanProviderImpl implements EnergyPlanProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
    public void createEnergyPlan(EnergyPlan plan) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyPlans.class));

        plan.setId(id);
        plan.setCreatorUid(UserContext.currentUserId());
        plan.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyPlans.class));
        EhEnergyPlansDao dao = new EhEnergyPlansDao(context.configuration());
        dao.insert(plan);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnergyPlans.class, id);

    }

    @Override
    public void updateEnergyPlan(EnergyPlan plan) {
        assert(plan.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyPlans.class));
        EhEnergyPlansDao dao = new EhEnergyPlansDao(context.configuration());
        plan.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        plan.setOperatorUid(UserContext.currentUserId());
        dao.update(plan);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnergyPlans.class, plan.getId());
    }

    @Override
    public EnergyPlan findEnergyPlanById(Long planId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyPlansDao dao = new EhEnergyPlansDao(context.configuration());
        return ConvertHelper.convert(dao.findById(planId), EnergyPlan.class);
    }

    @Override
    public List<EnergyPlan> listEnergyPlans(long pageAnchor, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_PLANS)
                .where(Tables.EH_ENERGY_PLANS.ID.ge(pageAnchor))
                .and(Tables.EH_ENERGY_PLANS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .limit(pageSize).fetchInto(EnergyPlan.class);
    }

    @Override
    public void createEnergyPlanGroupMap(EnergyPlanGroupMap groupMap) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyPlanGroupMap.class));

        groupMap.setId(id);
        groupMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyPlanGroupMap.class));
        EhEnergyPlanGroupMapDao dao = new EhEnergyPlanGroupMapDao(context.configuration());
        dao.insert(groupMap);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnergyPlanGroupMap.class, id);
    }

    @Override
    public void deleteEnergyPlanGroupMap(EnergyPlanGroupMap groupMap) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyPlanGroupMap.class));
        EhEnergyPlanGroupMapDao dao = new EhEnergyPlanGroupMapDao(context.configuration());
        dao.delete(groupMap);
    }

    @Override
    public List<EnergyPlanGroupMap> listGroupsByEnergyPlan(Long planId) {
        List<EnergyPlanGroupMap> map = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnergyPlanGroupMapRecord> query = context.selectQuery(Tables.EH_ENERGY_PLAN_GROUP_MAP);
        query.addConditions(Tables.EH_ENERGY_PLAN_GROUP_MAP.PLAN_ID.eq(planId));
        query.fetch().map((r) -> {
            map.add(ConvertHelper.convert(r, EnergyPlanGroupMap.class));
            return null;
        });

        if(map.size()==0)
            return null;

        return map;
    }

    @Override
    public Map<Long, EnergyPlanGroupMap> listGroupMapsByEnergyPlan(Long planId) {
        Map<Long, EnergyPlanGroupMap> map = new HashMap<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnergyPlanGroupMapRecord> query = context.selectQuery(Tables.EH_ENERGY_PLAN_GROUP_MAP);
        query.addConditions(Tables.EH_ENERGY_PLAN_GROUP_MAP.PLAN_ID.eq(planId));
        query.fetch().map((r) -> {
            map.put(r.getId(), ConvertHelper.convert(r, EnergyPlanGroupMap.class));
            return null;
        });

        if(map.size()==0)
            return null;

        return map;
    }

    @Override
    public void createEnergyPlanMeterMap(EnergyPlanMeterMap meterMap) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyPlanMeterMap.class));

        meterMap.setId(id);
        meterMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyPlanMeterMap.class));
        EhEnergyPlanMeterMapDao dao = new EhEnergyPlanMeterMapDao(context.configuration());
        dao.insert(meterMap);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnergyPlanMeterMap.class, id);
    }

    @Override
    public void updateEnergyPlanMeterMap(EnergyPlanMeterMap meterMap) {
        assert(meterMap.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyPlanMeterMap.class));
        EhEnergyPlanMeterMapDao dao = new EhEnergyPlanMeterMapDao(context.configuration());
        dao.update(meterMap);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnergyPlanMeterMap.class, meterMap.getId());
    }

    @Override
    public void deleteEnergyPlanMeterMap(EnergyPlanMeterMap meterMap) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyPlanMeterMap.class));
        EhEnergyPlanMeterMapDao dao = new EhEnergyPlanMeterMapDao(context.configuration());
        dao.delete(meterMap);
    }

    @Override
    public List<EnergyPlanMeterMap> listMetersByEnergyPlan(Long planId) {
        List<EnergyPlanMeterMap> map = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnergyPlanMeterMapRecord> query = context.selectQuery(Tables.EH_ENERGY_PLAN_METER_MAP);
        query.addConditions(Tables.EH_ENERGY_PLAN_METER_MAP.PLAN_ID.eq(planId));
        query.fetch().map((r) -> {
            map.add(ConvertHelper.convert(r, EnergyPlanMeterMap.class));
            return null;
        });

        if(map.size()==0)
            return null;

        return map;
    }

    @Override
    public Map<Long, EnergyPlanMeterMap> listMeterMapsByEnergyPlan(Long planId) {
        Map<Long, EnergyPlanMeterMap> map = new HashMap<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnergyPlanMeterMapRecord> query = context.selectQuery(Tables.EH_ENERGY_PLAN_METER_MAP);
        query.addConditions(Tables.EH_ENERGY_PLAN_METER_MAP.PLAN_ID.eq(planId));
        query.fetch().map((r) -> {
            map.put(r.getId(), ConvertHelper.convert(r, EnergyPlanMeterMap.class));
            return null;
        });

        if(map.size()==0)
            return null;

        return map;
    }
}
