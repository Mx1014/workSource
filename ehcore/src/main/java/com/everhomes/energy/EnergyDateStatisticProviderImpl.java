package com.everhomes.energy;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyDateStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyDateStatistics;
import com.everhomes.server.schema.tables.records.EhEnergyDateStatisticsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

public class EnergyDateStatisticProviderImpl implements EnergyDateStatisticProvider {
	   @Autowired
	    private DbProvider dbProvider;

	    @Autowired
	    private ShardingProvider shardingProvider;

	    @Autowired
	    private SequenceProvider sequenceProvider;

	    @Override
	    public Long createEnergyDateStatistic(EnergyDateStatistic obj) {
	        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyDateStatistics.class);
					long id = sequenceProvider.getNextSequence(key);
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        obj.setId(id);
	        prepareObj(obj);
	        EhEnergyDateStatisticsDao dao = new EhEnergyDateStatisticsDao(context.configuration());
	        dao.insert(obj);
	        return id;
	    }

	    @Override
	    public void updateEnergyDateStatistic(EnergyDateStatistic obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhEnergyDateStatisticsDao dao = new EhEnergyDateStatisticsDao(context.configuration());
	        dao.update(obj);
	    }

	    @Override
	    public void deleteEnergyDateStatistic(EnergyDateStatistic obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhEnergyDateStatisticsDao dao = new EhEnergyDateStatisticsDao(context.configuration());
	        dao.deleteById(obj.getId());
	    }

	    @Override
	    public EnergyDateStatistic getEnergyDateStatisticById(Long id) {
	        try {
	        EnergyDateStatistic[] result = new EnergyDateStatistic[1];
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        result[0] = context.select().from(Tables.EH_ENERGY_DATE_STATISTICS)
	            .where(Tables.EH_ENERGY_DATE_STATISTICS.ID.eq(id))
	            .fetchAny().map((r) -> {
	                return ConvertHelper.convert(r, EnergyDateStatistic.class);
	            });

	        return result[0];
	        } catch (Exception ex) {
	            //fetchAny() maybe return null
	            return null;
	        }
	    }

	    @Override
	    public List<EnergyDateStatistic> queryEnergyDateStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        SelectQuery<EhEnergyDateStatisticsRecord> query = context.selectQuery(Tables.EH_ENERGY_DATE_STATISTICS);
	        if(queryBuilderCallback != null)
	            queryBuilderCallback.buildCondition(locator, query);

	        if(null != locator && locator.getAnchor() != null) {
	            query.addConditions(Tables.EH_ENERGY_DATE_STATISTICS.ID.gt(locator.getAnchor()));
	            }

	        query.addLimit(count);
	        List<EnergyDateStatistic> objs = query.fetch().map((r) -> {
	            return ConvertHelper.convert(r, EnergyDateStatistic.class);
	        });

	        return objs;
	    }

	    private void prepareObj(EnergyDateStatistic obj) {
	    }
}
