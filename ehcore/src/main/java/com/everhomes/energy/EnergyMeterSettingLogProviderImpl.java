package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyMeterSettingType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterSettingLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterSettingLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_SETTING_LOGS;

/**
 * Created by xq.tian on 2016/10/31.
 */
@Repository
public class EnergyMeterSettingLogProviderImpl implements EnergyMeterSettingLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public long createSettingLog(EnergyMeterSettingLog log) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterSettingLogs.class));
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setCreatorUid(UserContext.current().getUser().getId());
        log.setId(id);
        rwDao().insert(log);
        return id;
    }

    @Override
    public EnergyMeterSettingLog findCurrentSettingByMeterId(Integer namespaceId, Long meterId, EnergyMeterSettingType settingType) {
        return context().selectFrom(EH_ENERGY_METER_SETTING_LOGS)
                .where(EH_ENERGY_METER_SETTING_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_SETTING_LOGS.METER_ID.eq(meterId))
                .and(EH_ENERGY_METER_SETTING_LOGS.SETTING_TYPE.eq(settingType.getCode()))
                .and(EH_ENERGY_METER_SETTING_LOGS.START_TIME.le(Timestamp.valueOf(LocalDateTime.now())))
                .and(EH_ENERGY_METER_SETTING_LOGS.END_TIME.ge(Timestamp.valueOf(LocalDateTime.now())))
                .orderBy(EH_ENERGY_METER_SETTING_LOGS.CREATE_TIME.desc())
                .fetchAnyInto(EnergyMeterSettingLog.class);
    }

    @Override
    public EnergyMeterSettingLog findCurrentSettingByMeterId(Integer namespaceId, Long meterId, EnergyMeterSettingType settingType ,Timestamp statDate) {
        return context().selectFrom(EH_ENERGY_METER_SETTING_LOGS)
                .where(EH_ENERGY_METER_SETTING_LOGS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_SETTING_LOGS.METER_ID.eq(meterId))
                .and(EH_ENERGY_METER_SETTING_LOGS.SETTING_TYPE.eq(settingType.getCode()))
                .and(EH_ENERGY_METER_SETTING_LOGS.START_TIME.le(statDate))
                .and(EH_ENERGY_METER_SETTING_LOGS.END_TIME.ge(statDate))
                .orderBy(EH_ENERGY_METER_SETTING_LOGS.CREATE_TIME.desc())
                .fetchAnyInto(EnergyMeterSettingLog.class);
    }
    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterSettingLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterSettingLogsDao(context.configuration());
    }
}
