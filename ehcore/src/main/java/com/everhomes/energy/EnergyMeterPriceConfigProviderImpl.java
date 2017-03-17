package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterPriceConfigDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterPriceConfig;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ying.xiong on 2017/3/16.
 */
@Component
public class EnergyMeterPriceConfigProviderImpl implements EnergyMeterPriceConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createEnergyMeterPriceConfig(EnergyMeterPriceConfig config) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterPriceConfig.class));
        config.setId(id);
        config.setCreatorUid(UserContext.current().getUser().getId());
        config.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        rwDao().insert(config);
        return id;
    }

    @Override
    public EnergyMeterPriceConfig findById(Long id, Long ownerId, String ownerType, Long communityId, Integer namespaceId) {
        return context().selectFrom(Tables.EH_ENERGY_METER_PRICE_CONFIG)
                .where(Tables.EH_ENERGY_METER_PRICE_CONFIG.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.ID.eq(id))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.OWNER_ID.eq(ownerId))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.COMMUNITY_ID.eq(communityId))
                .fetchOneInto(EnergyMeterPriceConfig.class);
    }

    @Override
    public EnergyMeterPriceConfig findById(Long id) {
        return context().selectFrom(Tables.EH_ENERGY_METER_PRICE_CONFIG)
                .where(Tables.EH_ENERGY_METER_PRICE_CONFIG.ID.eq(id))
                .fetchOneInto(EnergyMeterPriceConfig.class);
    }

    @Override
    public EnergyMeterPriceConfig findByName(String name, Long ownerId, String ownerType, Long communityId, Integer namespaceId) {
        return context().selectFrom(Tables.EH_ENERGY_METER_PRICE_CONFIG)
                .where(Tables.EH_ENERGY_METER_PRICE_CONFIG.NAME.eq(name))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.OWNER_ID.eq(ownerId))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.NAMESPACE_ID.eq(namespaceId))
                .fetchOneInto(EnergyMeterPriceConfig.class);
    }

    @Override
    public List<EnergyMeterPriceConfig> listPriceConfig(Long ownerId, String ownerType, Long communityId, Integer namespaceId) {
        return context().selectFrom(Tables.EH_ENERGY_METER_PRICE_CONFIG)
                .where(Tables.EH_ENERGY_METER_PRICE_CONFIG.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.OWNER_ID.eq(ownerId))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_ENERGY_METER_PRICE_CONFIG.COMMUNITY_ID.eq(communityId))
                .fetchInto(EnergyMeterPriceConfig.class);
    }

    @Override
    public void deletePriceConfig(EnergyMeterPriceConfig config) {
        rwDao().delete(config);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterPriceConfigDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterPriceConfigDao(context.configuration());
    }
}
