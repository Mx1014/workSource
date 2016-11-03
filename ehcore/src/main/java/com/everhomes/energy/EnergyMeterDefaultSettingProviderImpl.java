package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterDefaultSettingsDao;
import com.everhomes.server.schema.tables.records.EhEnergyMeterDefaultSettingsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_DEFAULT_SETTINGS;

/**
 * Created by xq.tian on 2016/11/2.
 */
@Repository
public class EnergyMeterDefaultSettingProviderImpl implements EnergyMeterDefaultSettingProvider {

    @Autowired
    private DbProvider dbProvider;

    // @Autowired
    // private SequenceProvider sequenceProvider;

    @Override
    public EnergyMeterDefaultSetting findById(Integer namespaceId, Long id) {
        return context().selectFrom(EH_ENERGY_METER_DEFAULT_SETTINGS)
                .where(EH_ENERGY_METER_DEFAULT_SETTINGS.NAMESPACE_ID.eq(namespaceId))
                // .and(EH_ENERGY_METER_DEFAULT_SETTINGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .and(EH_ENERGY_METER_DEFAULT_SETTINGS.ID.eq(id))
                .fetchOneInto(EnergyMeterDefaultSetting.class);
    }

    @Override
    public void updateEnergyMeterDefaultSetting(EnergyMeterDefaultSetting setting) {
        setting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        setting.setUpdateUid(UserContext.current().getUser().getId());
        rwDao().update(setting);
    }

    @Override
    public List<EnergyMeterDefaultSetting> listDefaultSetting(Integer namespaceId, Byte meterType) {
        SelectQuery<EhEnergyMeterDefaultSettingsRecord> query = context().selectFrom(EH_ENERGY_METER_DEFAULT_SETTINGS)
                .where(EH_ENERGY_METER_DEFAULT_SETTINGS.NAMESPACE_ID.eq(namespaceId)).getQuery();
        if (meterType != null) {
            query.addConditions(EH_ENERGY_METER_DEFAULT_SETTINGS.METER_TYPE.eq(meterType));
        }
        query.addConditions(EH_ENERGY_METER_DEFAULT_SETTINGS.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()));
        return query.fetchInto(EnergyMeterDefaultSetting.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterDefaultSettingsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterDefaultSettingsDao(context.configuration());
    }
}
