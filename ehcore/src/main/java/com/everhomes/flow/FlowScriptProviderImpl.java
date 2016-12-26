package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowScriptsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowScripts;
import com.everhomes.server.schema.tables.records.EhFlowScriptsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowScriptProviderImpl implements FlowScriptProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowScript(FlowScript obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowScripts.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowScript(FlowScript obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));
        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowScript(FlowScript obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));
        EhFlowScriptsDao dao = new EhFlowScriptsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowScript getFlowScriptById(Long id) {
        try {
        FlowScript[] result = new FlowScript[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));

        result[0] = context.select().from(Tables.EH_FLOW_SCRIPTS)
            .where(Tables.EH_FLOW_SCRIPTS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowScript.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowScript> queryFlowScripts(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowScripts.class));

        SelectQuery<EhFlowScriptsRecord> query = context.selectQuery(Tables.EH_FLOW_SCRIPTS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_SCRIPTS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowScript> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowScript.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowScript obj) {
    }
    
    @Override
    public List<FlowScript>  findFlowScriptByModuleId(Long moduleId, String moduleType) {
    	ListingLocator locator = new ListingLocator();
    	return queryFlowScripts(locator, 100, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_SCRIPTS.MODULE_ID.eq(moduleId));
				if(moduleType != null) {
					query.addConditions(Tables.EH_FLOW_SCRIPTS.MODULE_TYPE.eq(moduleType));	
				}
				
				return query;
			}
    		
    	});
    }
}

