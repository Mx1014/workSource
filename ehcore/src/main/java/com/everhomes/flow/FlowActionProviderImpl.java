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

import com.everhomes.rest.flow.FlowActionStatus;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowActionsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowActions;
import com.everhomes.server.schema.tables.records.EhFlowActionsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowActionProviderImpl implements FlowActionProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowAction(FlowAction obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowActions.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowActions.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowActionsDao dao = new EhFlowActionsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowAction(FlowAction obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowActions.class));
        EhFlowActionsDao dao = new EhFlowActionsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowAction(FlowAction obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowActions.class));
        EhFlowActionsDao dao = new EhFlowActionsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowAction getFlowActionById(Long id) {
        try {
        FlowAction[] result = new FlowAction[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowActions.class));

        result[0] = context.select().from(Tables.EH_FLOW_ACTIONS)
            .where(Tables.EH_FLOW_ACTIONS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowAction.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowAction> queryFlowActions(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowActions.class));

        SelectQuery<EhFlowActionsRecord> query = context.selectQuery(Tables.EH_FLOW_ACTIONS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_ACTIONS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowAction> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowAction.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowAction obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    public FlowAction findFlowActionByBelong(Long belong, String entityType, String actionType, String actionStepType, String flowStepType) {
    	ListingLocator locator = new ListingLocator();
    	List<FlowAction> flowActions = this.queryFlowActions(locator, 1, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_ACTIONS.BELONG_TO.eq(belong));
				query.addConditions(Tables.EH_FLOW_ACTIONS.BELONG_ENTITY.eq(entityType));
				query.addConditions(Tables.EH_FLOW_ACTIONS.ACTION_TYPE.eq(actionType));
				query.addConditions(Tables.EH_FLOW_ACTIONS.ACTION_STEP_TYPE.eq(actionStepType));
				if(flowStepType != null) {
					query.addConditions(Tables.EH_FLOW_ACTIONS.FLOW_STEP_TYPE.eq(flowStepType));	
				}
				query.addConditions(Tables.EH_FLOW_ACTIONS.STATUS.ne(FlowActionStatus.INVALID.getCode()));
				return query;
			}
    	});
    	
    	if(flowActions != null && flowActions.size() != 0) {
    		return flowActions.get(0);
    	}
    	
    	return null;
    }
    
    @Override
    public List<FlowAction> findFlowActionsByBelong(Long belong, String entityType, String actionType, String actionStepType, String flowStepType) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowActions(locator, 100, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_ACTIONS.BELONG_TO.eq(belong));
				query.addConditions(Tables.EH_FLOW_ACTIONS.BELONG_ENTITY.eq(entityType));
				query.addConditions(Tables.EH_FLOW_ACTIONS.ACTION_TYPE.eq(actionType));
				query.addConditions(Tables.EH_FLOW_ACTIONS.ACTION_STEP_TYPE.eq(actionStepType));
				if(flowStepType != null) {
					query.addConditions(Tables.EH_FLOW_ACTIONS.FLOW_STEP_TYPE.eq(flowStepType));	
				}
				query.addConditions(Tables.EH_FLOW_ACTIONS.STATUS.ne(FlowActionStatus.INVALID.getCode()));
				return query;
			}
    	});
    }
}
