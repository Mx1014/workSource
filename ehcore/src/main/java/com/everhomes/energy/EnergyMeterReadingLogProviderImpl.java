package com.everhomes.energy;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterReadingLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterReadingLogs;
import com.everhomes.server.schema.tables.records.EhEnergyMeterReadingLogsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class EnergyMeterReadingLogProviderImpl implements EnergyMeterReadingLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createEnergyMeterReadingLog(EnergyMeterReadingLog obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterReadingLogs.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateEnergyMeterReadingLog(EnergyMeterReadingLog obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteEnergyMeterReadingLog(EnergyMeterReadingLog obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMeterReadingLogsDao dao = new EhEnergyMeterReadingLogsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public EnergyMeterReadingLog getEnergyMeterReadingLogById(Long id) {
        try {
        EnergyMeterReadingLog[] result = new EnergyMeterReadingLog[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_ENERGY_METER_READING_LOGS)
            .where(Tables.EH_ENERGY_METER_READING_LOGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, EnergyMeterReadingLog.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<EnergyMeterReadingLog> queryEnergyMeterReadingLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhEnergyMeterReadingLogsRecord> query = context.selectQuery(Tables.EH_ENERGY_METER_READING_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENERGY_METER_READING_LOGS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<EnergyMeterReadingLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnergyMeterReadingLog.class);
        });

        return objs;
    }

    private void prepareObj(EnergyMeterReadingLog obj) {
    }
}
