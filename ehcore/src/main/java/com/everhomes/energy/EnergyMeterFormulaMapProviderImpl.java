package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterFomularMapDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFomularMap;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ying.xiong on 2017/12/11.
 */
public class EnergyMeterFormulaMapProviderImpl implements EnergyMeterFormulaMapProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createEnergyMeterFormulaMap(EnergyMeterFormulaMap map) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterFomularMap.class));
        map.setId(id);
        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        map.setStatus(EnergyCommonStatus.ACTIVE.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterFomularMapDao dao = new EhEnergyMeterFomularMapDao(context.configuration());
        dao.insert(map);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnergyMeterFomularMap.class, id);
    }

    @Override
    public void updateEnergyMeterFormulaMap(EnergyMeterFormulaMap map) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterFomularMapDao dao = new EhEnergyMeterFomularMapDao(context.configuration());
        dao.update(map);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnergyMeterFomularMap.class, map.getId());
    }

    @Override
    public List<EnergyMeterFormulaMap> listEnergyMeterFormulaMap(Long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<EnergyMeterFormulaMap> maps = context.select().from(Tables.EH_ENERGY_METER_FOMULAR_MAP)
                .where(Tables.EH_ENERGY_METER_FOMULAR_MAP.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ENERGY_METER_FOMULAR_MAP.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, EnergyMeterFormulaMap.class);
                });

        return maps;
    }

    @Override
    public EnergyMeterFormulaMap findEnergyMeterFormulaMap(Long community, Long formulaId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ENERGY_METER_FOMULAR_MAP)
                .where(Tables.EH_ENERGY_METER_FOMULAR_MAP.FOMULAR_ID.eq(formulaId))
                .and(Tables.EH_ENERGY_METER_FOMULAR_MAP.COMMUNITY_ID.eq(community))
                .and(Tables.EH_ENERGY_METER_FOMULAR_MAP.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .fetchAnyInto(EnergyMeterFormulaMap.class);
    }
}
