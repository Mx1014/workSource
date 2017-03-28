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

import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowVariablesDao;
import com.everhomes.server.schema.tables.pojos.EhFlowVariables;
import com.everhomes.server.schema.tables.records.EhFlowVariablesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowVariableProviderImpl implements FlowVariableProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowVariable(FlowVariable obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowVariables.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowVariables.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowVariablesDao dao = new EhFlowVariablesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowVariable(FlowVariable obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowVariables.class));
        EhFlowVariablesDao dao = new EhFlowVariablesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowVariable(FlowVariable obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowVariables.class));
        EhFlowVariablesDao dao = new EhFlowVariablesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowVariable getFlowVariableById(Long id) {
        try {
        FlowVariable[] result = new FlowVariable[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowVariables.class));

        result[0] = context.select().from(Tables.EH_FLOW_VARIABLES)
            .where(Tables.EH_FLOW_VARIABLES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowVariable.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowVariable> queryFlowVariables(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowVariables.class));

        SelectQuery<EhFlowVariablesRecord> query = context.selectQuery(Tables.EH_FLOW_VARIABLES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_VARIABLES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowVariable> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowVariable.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowVariable obj) {
    }
    
    @Override
    public List<FlowVariable> findVariables(Integer namespaceId, Long ownerId
    		, String ownerType, Long moduleId
    		, String moduleType, String name, String varType) {
    	return queryFlowVariables(new ListingLocator(), 50, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_VARIABLES.NAMESPACE_ID.eq(namespaceId));
				query.addConditions(Tables.EH_FLOW_VARIABLES.OWNER_ID.eq(ownerId));
				if(ownerType != null) {
					query.addConditions(Tables.EH_FLOW_VARIABLES.OWNER_TYPE.eq(ownerType));	
				}
				
				query.addConditions(Tables.EH_FLOW_VARIABLES.MODULE_ID.eq(moduleId));
				
				if(moduleType != null) {
					query.addConditions(Tables.EH_FLOW_VARIABLES.MODULE_TYPE.eq(moduleType));	
				}
				
				query.addConditions(Tables.EH_FLOW_VARIABLES.VAR_TYPE.contains(varType));
				
				if(name != null) {
					query.addConditions(Tables.EH_FLOW_VARIABLES.NAME.eq(name));	
				}
				
				query.addConditions(Tables.EH_FLOW_VARIABLES.STATUS.eq(FlowStatusType.VALID.getCode()));
				
				return query;
			}
    		
    	});
    }
}
