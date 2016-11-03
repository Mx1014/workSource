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
import com.everhomes.server.schema.tables.daos.EhEnergyCountStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyCountStatistics;
import com.everhomes.server.schema.tables.records.EhEnergyCountStatisticsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;


@Component
public class EnergyCountStatisticProviderImpl implements EnergyCountStatisticProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createEnergyCountStatistic(EnergyCountStatistic obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyCountStatistics.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhEnergyCountStatisticsDao dao = new EhEnergyCountStatisticsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateEnergyCountStatistic(EnergyCountStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyCountStatisticsDao dao = new EhEnergyCountStatisticsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteEnergyCountStatistic(EnergyCountStatistic obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnergyCountStatisticsDao dao = new EhEnergyCountStatisticsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public EnergyCountStatistic getEnergyCountStatisticById(Long id) {
        try {
        EnergyCountStatistic[] result = new EnergyCountStatistic[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_ENERGY_COUNT_STATISTICS)
            .where(Tables.EH_ENERGY_COUNT_STATISTICS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, EnergyCountStatistic.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<EnergyCountStatistic> queryEnergyCountStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhEnergyCountStatisticsRecord> query = context.selectQuery(Tables.EH_ENERGY_COUNT_STATISTICS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENERGY_COUNT_STATISTICS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<EnergyCountStatistic> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, EnergyCountStatistic.class);
        });

        return objs;
    }

    private void prepareObj(EnergyCountStatistic obj) {
    }
}