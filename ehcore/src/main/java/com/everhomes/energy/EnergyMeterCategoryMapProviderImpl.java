package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterCategoryMapDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterCategoryMap;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ying.xiong on 2017/12/11.
 */
public class EnergyMeterCategoryMapProviderImpl implements EnergyMeterCategoryMapProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createEnergyMeterCategoryMap(EnergyMeterCategoryMap map) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterCategoryMap.class));
        map.setId(id);
        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        map.setStatus(EnergyCommonStatus.ACTIVE.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterCategoryMapDao dao = new EhEnergyMeterCategoryMapDao(context.configuration());
        dao.insert(map);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnergyMeterCategoryMap.class, id);

    }

    @Override
    public void updateEnergyMeterCategoryMap(EnergyMeterCategoryMap map) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterCategoryMapDao dao = new EhEnergyMeterCategoryMapDao(context.configuration());
        dao.update(map);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnergyMeterCategoryMap.class, map.getId());
    }

    @Override
    public List<EnergyMeterCategoryMap> listEnergyMeterCategoryMap(Long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<EnergyMeterCategoryMap> maps = context.select().from(Tables.EH_ENERGY_METER_CATEGORY_MAP)
                .where(Tables.EH_ENERGY_METER_CATEGORY_MAP.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ENERGY_METER_CATEGORY_MAP.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, EnergyMeterCategoryMap.class);
                });

        return maps;
    }

    @Override
    public EnergyMeterCategoryMap findEnergyMeterCategoryMap(Long community, Long categoryId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ENERGY_METER_CATEGORY_MAP)
                .where(Tables.EH_ENERGY_METER_CATEGORY_MAP.CATEGORY_ID.eq(categoryId))
                .and(Tables.EH_ENERGY_METER_CATEGORY_MAP.COMMUNITY_ID.eq(community))
                .and(Tables.EH_ENERGY_METER_CATEGORY_MAP.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .fetchAnyInto(EnergyMeterCategoryMap.class);
    }
}
