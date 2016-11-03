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
import com.everhomes.server.schema.tables.daos.EhEnergyYoyStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyYoyStatistics;
import com.everhomes.server.schema.tables.records.EhEnergyYoyStatisticsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class EnergyYoyStatisticProviderImpl implements EnergyYoyStatisticProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createEnergyYoyStatistic(EnergyYoyStatistic obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyYoyStatistics.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhEnergyYoyStatisticsDao dao = new EhEnergyYoyStatisticsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateEnergyYoyStatistic(EnergyYoyStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyYoyStatisticsDao dao = new EhEnergyYoyStatisticsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteEnergyYoyStatistic(EnergyYoyStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyYoyStatisticsDao dao = new EhEnergyYoyStatisticsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public EnergyYoyStatistic getEnergyYoyStatisticById(Long id) {
        try {
        EnergyYoyStatistic[] result = new EnergyYoyStatistic[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_ENERGY_YOY_STATISTICS)
            .where(Tables.EH_ENERGY_YOY_STATISTICS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, EnergyYoyStatistic.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<EnergyYoyStatistic> queryEnergyYoyStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhEnergyYoyStatisticsRecord> query = context.selectQuery(Tables.EH_ENERGY_YOY_STATISTICS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENERGY_YOY_STATISTICS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<EnergyYoyStatistic> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnergyYoyStatistic.class);
        });

        return objs;
    }

    private void prepareObj(EnergyYoyStatistic obj) {
    }
}