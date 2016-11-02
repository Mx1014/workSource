package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterChangeLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterChangeLogs;
import com.everhomes.server.schema.tables.records.EhEnergyMeterChangeLogsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnergyMeterChangeLogProviderImpl implements EnergyMeterChangeLogProvider {
	  @Autowired
	    private DbProvider dbProvider;

	    @Autowired
	    private ShardingProvider shardingProvider;

	    @Autowired
	    private SequenceProvider sequenceProvider;

	    @Override
	    public Long createEnergyMeterChangeLog(EnergyMeterChangeLog obj) {
	        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterChangeLogs.class);
					long id = sequenceProvider.getNextSequence(key);
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        obj.setId(id);
	        prepareObj(obj);
	        EhEnergyMeterChangeLogsDao dao = new EhEnergyMeterChangeLogsDao(context.configuration());
	        dao.insert(obj);
	        return id;
	    }

	    @Override
	    public void updateEnergyMeterChangeLog(EnergyMeterChangeLog obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhEnergyMeterChangeLogsDao dao = new EhEnergyMeterChangeLogsDao(context.configuration());
	        dao.update(obj);
	    }

	    @Override
	    public void deleteEnergyMeterChangeLog(EnergyMeterChangeLog obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhEnergyMeterChangeLogsDao dao = new EhEnergyMeterChangeLogsDao(context.configuration());
	        dao.deleteById(obj.getId());
	    }

	    @Override
	    public EnergyMeterChangeLog getEnergyMeterChangeLogById(Long id) {
	        try {
	        EnergyMeterChangeLog[] result = new EnergyMeterChangeLog[1];
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        result[0] = context.select().from(Tables.EH_ENERGY_METER_CHANGE_LOGS)
	            .where(Tables.EH_ENERGY_METER_CHANGE_LOGS.ID.eq(id))
	            .fetchAny().map((r) -> {
	                return ConvertHelper.convert(r, EnergyMeterChangeLog.class);
	            });

	        return result[0];
	        } catch (Exception ex) {
	            //fetchAny() maybe return null
	            return null;
	        }
	    }

	    @Override
	    public List<EnergyMeterChangeLog> queryEnergyMeterChangeLogs(ListingLocator locator, int count, 
	    		ListingQueryBuilderCallback queryBuilderCallback) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        SelectQuery<EhEnergyMeterChangeLogsRecord> query = context.selectQuery(Tables.EH_ENERGY_METER_CHANGE_LOGS);
	        if(queryBuilderCallback != null)
	            queryBuilderCallback.buildCondition(locator, query);

	        if(null != locator && locator.getAnchor() != null) {
	            query.addConditions(Tables.EH_ENERGY_METER_CHANGE_LOGS.ID.gt(locator.getAnchor()));
	            }

	        query.addLimit(count);
	        List<EnergyMeterChangeLog> objs = query.fetch().map((r) -> {
	            return ConvertHelper.convert(r, EnergyMeterChangeLog.class);
	        });

	        return objs;
	    }

    @Override
    public EnergyMeterChangeLog getEnergyMeterChangeLogByLogId(Long id) {
        return null;
    }

    private void prepareObj(EnergyMeterChangeLog obj) {
	    }
}
