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
import com.everhomes.server.schema.tables.daos.EhEnergyMonthStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMonthStatistics;
import com.everhomes.server.schema.tables.records.EhEnergyMonthStatisticsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

 

@Component
public class EnergyMonthStatisticProviderImpl implements EnergyMonthStatisticProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createEnergyMonthStatistic(EnergyMonthStatistic obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyMonthStatistics.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhEnergyMonthStatisticsDao dao = new EhEnergyMonthStatisticsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateEnergyMonthStatistic(EnergyMonthStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMonthStatisticsDao dao = new EhEnergyMonthStatisticsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteEnergyMonthStatistic(EnergyMonthStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyMonthStatisticsDao dao = new EhEnergyMonthStatisticsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public EnergyMonthStatistic getEnergyMonthStatisticById(Long id) {
        try {
        EnergyMonthStatistic[] result = new EnergyMonthStatistic[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_ENERGY_MONTH_STATISTICS)
            .where(Tables.EH_ENERGY_MONTH_STATISTICS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, EnergyMonthStatistic.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<EnergyMonthStatistic> queryEnergyMonthStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhEnergyMonthStatisticsRecord> query = context.selectQuery(Tables.EH_ENERGY_MONTH_STATISTICS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENERGY_MONTH_STATISTICS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<EnergyMonthStatistic> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnergyMonthStatistic.class);
        });

        return objs;
    }

    private void prepareObj(EnergyMonthStatistic obj) {
    }

	@Override
	public void deleteEnergyMonthStatisticByDate(Long meterId, String monthStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_ENERGY_MONTH_STATISTICS).where(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.eq(monthStr))
				.and(Tables.EH_ENERGY_MONTH_STATISTICS.METER_ID.eq(meterId)).execute();
	}
}