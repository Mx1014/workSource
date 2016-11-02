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
import com.everhomes.server.schema.tables.daos.EhEnergyYearStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyYearStatistics;
import com.everhomes.server.schema.tables.records.EhEnergyYearStatisticsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class EnergyYearStatisticProviderImpl implements EnergyYearStatisticProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createEnergyYearStatistic(EnergyYearStatistic obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyYearStatistics.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhEnergyYearStatisticsDao dao = new EhEnergyYearStatisticsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateEnergyYearStatistic(EnergyYearStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyYearStatisticsDao dao = new EhEnergyYearStatisticsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteEnergyYearStatistic(EnergyYearStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyYearStatisticsDao dao = new EhEnergyYearStatisticsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public EnergyYearStatistic getEnergyYearStatisticById(Long id) {
        try {
        EnergyYearStatistic[] result = new EnergyYearStatistic[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_ENERGY_YEAR_STATISTICS)
            .where(Tables.EH_ENERGY_YEAR_STATISTICS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, EnergyYearStatistic.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<EnergyYearStatistic> queryEnergyYearStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhEnergyYearStatisticsRecord> query = context.selectQuery(Tables.EH_ENERGY_YEAR_STATISTICS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENERGY_YEAR_STATISTICS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<EnergyYearStatistic> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnergyYearStatistic.class);
        });

        return objs;
    }

    private void prepareObj(EnergyYearStatistic obj) {
    }
}