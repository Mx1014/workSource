package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterCategoriesDao;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_CATEGORIES;

/**
 * Created by xq.tian on 2016/10/31.
 */
@Repository
public class EnergyMeterCategoryProviderImpl implements EnergyMeterCategoryProvider {

    @Autowired
    private DbProvider dbProvider;

    // @Autowired
    // private SequenceProvider sequenceProvider;

    @Override
    public EnergyMeterCategory findById(Integer namespaceId, Long id) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_CATEGORIES.ID.eq(id))
                .fetchOneInto(EnergyMeterCategory.class);
    }

    @Override
    public EnergyMeterCategory findById( Long id) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.ID.eq(id))
                .fetchOneInto(EnergyMeterCategory.class);
    }

    @Override
    public EnergyMeterCategory findByName(Integer namespaceId, String name) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_CATEGORIES.NAME.eq(name))
                .fetchAnyInto(EnergyMeterCategory.class);
    }

    @Override
    public List<EnergyMeterCategory> listMeterCategories(Integer namespaceId, Byte categoryType) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_CATEGORIES.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .and(EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(categoryType))
                .fetchInto(EnergyMeterCategory.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterCategoriesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterCategoriesDao(context.configuration());
    }
}
