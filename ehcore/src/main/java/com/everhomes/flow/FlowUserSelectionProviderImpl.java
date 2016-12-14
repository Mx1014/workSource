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

import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.FlowUserSelectionType;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowUserSelectionsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowUserSelections;
import com.everhomes.server.schema.tables.records.EhFlowUserSelectionsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowUserSelectionProviderImpl implements FlowUserSelectionProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowUserSelection(FlowUserSelection obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowUserSelections.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowUserSelectionsDao dao = new EhFlowUserSelectionsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowUserSelection(FlowUserSelection obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));
        EhFlowUserSelectionsDao dao = new EhFlowUserSelectionsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowUserSelection(FlowUserSelection obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));
        EhFlowUserSelectionsDao dao = new EhFlowUserSelectionsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowUserSelection getFlowUserSelectionById(Long id) {
        try {
        FlowUserSelection[] result = new FlowUserSelection[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));

        result[0] = context.select().from(Tables.EH_FLOW_USER_SELECTIONS)
            .where(Tables.EH_FLOW_USER_SELECTIONS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowUserSelection.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowUserSelection> queryFlowUserSelections(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));

        SelectQuery<EhFlowUserSelectionsRecord> query = context.selectQuery(Tables.EH_FLOW_USER_SELECTIONS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowUserSelection> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowUserSelection.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowUserSelection obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    public List<FlowUserSelection> findSelectionByBelong(Long belongId, String belongEntity, String flowUserBelongType) {
    	return findSelectionByBelong(belongId, belongEntity, flowUserBelongType, null);
    }
    
    @Override
    public List<FlowUserSelection> findSelectionByBelong(Long belongId, String belongEntity, String flowUserBelongType, Integer ver) {
    	ListingLocator locator = new ListingLocator();
    	return queryFlowUserSelections(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.BELONG_TO.eq(belongId));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.BELONG_ENTITY.eq(belongEntity));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.BELONG_TYPE.eq(flowUserBelongType));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.STATUS.ne(FlowStatusType.INVALID.getCode()));
				if(ver != null) {
					query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.FLOW_VERSION.eq(ver));
				}
				return query;
			}
    		
    	});
    }
    
    @Override
    public List<FlowUserSelection> deleteSelectionByBelong(Long belongId, String belongEntity, String flowUserBelongType) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));
    	EhFlowUserSelectionsDao dao = new EhFlowUserSelectionsDao(context.configuration());
    	List<FlowUserSelection> seles = findSelectionByBelong(belongId, belongEntity, flowUserBelongType);
    	if(seles != null && seles.size() > 0) {
    		dao.delete(seles.toArray(new FlowUserSelection[seles.size()]));	
    	}
    	
    	return seles;
    }
    
    @Override
    public FlowUserSelection findFlowNodeSelectionUser(Long flowNodeId, Integer ver, Long userId) {
    	ListingLocator locator = new ListingLocator();
    	List<FlowUserSelection> seles = queryFlowUserSelections(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.BELONG_TO.eq(flowNodeId));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.BELONG_ENTITY.eq(FlowEntityType.FLOW_NODE.getCode()));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.BELONG_TYPE.eq(FlowUserType.PROCESSOR.getCode()));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.STATUS.ne(FlowStatusType.INVALID.getCode()));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.SOURCE_ID_A.eq(userId));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.SELECT_TYPE.eq(FlowUserSelectionType.DEPARTMENT.getCode()));
				query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.SOURCE_TYPE_A.eq(FlowUserSourceType.SOURCE_USER.getCode()));
				if(ver != null) {
					query.addConditions(Tables.EH_FLOW_USER_SELECTIONS.FLOW_VERSION.eq(ver));
				}
				return query;
			}
    		
    	});
    	
    	if(seles != null && seles.size() > 0) {
    		return seles.get(0);
    	}
    	
    	return null;
    }
    
    @Override
    public void createFlowUserSelections(List<FlowUserSelection> objs) {
    	if(objs == null || objs.size() == 0) {
    		return;
    	}
    	
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));
        objs.stream().forEach((obj)->{
        	long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowUserSelections.class));
        	obj.setId(id);
        	prepareObj(obj);
        });
        
        EhFlowUserSelectionsDao dao = new EhFlowUserSelectionsDao(context.configuration());
        dao.insert(objs.toArray(new FlowUserSelection[objs.size()]));
    }
    
    @Override
    public void deleteFlowUserSelections(List<FlowUserSelection> objs) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowUserSelections.class));
        EhFlowUserSelectionsDao dao = new EhFlowUserSelectionsDao(context.configuration());
        dao.delete(objs.toArray(new FlowUserSelection[objs.size()]));
    }
}
