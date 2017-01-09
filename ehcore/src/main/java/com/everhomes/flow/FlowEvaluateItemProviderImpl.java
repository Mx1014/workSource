package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
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
import com.everhomes.server.schema.tables.daos.EhFlowEvaluateItemsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowEvaluateItems;
import com.everhomes.server.schema.tables.records.EhFlowEvaluateItemsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class FlowEvaluateItemProviderImpl implements FlowEvaluateItemProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowEvaluateItem(FlowEvaluateItem obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowEvaluateItems.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluateItems.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowEvaluateItemsDao dao = new EhFlowEvaluateItemsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowEvaluateItem(FlowEvaluateItem obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluateItems.class));
        EhFlowEvaluateItemsDao dao = new EhFlowEvaluateItemsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowEvaluateItem(FlowEvaluateItem obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluateItems.class));
        EhFlowEvaluateItemsDao dao = new EhFlowEvaluateItemsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowEvaluateItem getFlowEvaluateItemById(Long id) {
        try {
        FlowEvaluateItem[] result = new FlowEvaluateItem[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluateItems.class));

        result[0] = context.select().from(Tables.EH_FLOW_EVALUATE_ITEMS)
            .where(Tables.EH_FLOW_EVALUATE_ITEMS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowEvaluateItem.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowEvaluateItem> queryFlowEvaluateItems(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluateItems.class));

        SelectQuery<EhFlowEvaluateItemsRecord> query = context.selectQuery(Tables.EH_FLOW_EVALUATE_ITEMS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_EVALUATE_ITEMS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowEvaluateItem> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowEvaluateItem.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowEvaluateItem obj) {
    }
    
    @Override
    public List<FlowEvaluateItem> findFlowEvaluateItemsByFlowId(Long flowId, Integer flowVer) {
    	ListingLocator locator = new ListingLocator();
    	return queryFlowEvaluateItems(locator, 100, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVALUATE_ITEMS.FLOW_MAIN_ID.eq(flowId));
				query.addConditions(Tables.EH_FLOW_EVALUATE_ITEMS.FLOW_VERSION.eq(flowVer));
				return query;
			}
    		
    	});
    }
    
    @Override
    public void deleteFlowEvaluateItem(List<FlowEvaluateItem> objs) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluateItems.class));
        EhFlowEvaluateItemsDao dao = new EhFlowEvaluateItemsDao(context.configuration());
        dao.delete(objs.toArray(new FlowEvaluateItem[objs.size()]));
    }
    
    @Override
    public void createFlowEvaluateItem(List<FlowEvaluateItem> objs) {
    	for(FlowEvaluateItem obj : objs) {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowEvaluateItems.class));
            obj.setId(id);
            prepareObj(obj);    		
    	}
    	
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluateItems.class));
    	EhFlowEvaluateItemsDao dao = new EhFlowEvaluateItemsDao(context.configuration());
    	dao.insert(objs.toArray(new FlowEvaluateItem[objs.size()]));
    }
}
