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

import com.everhomes.rest.flow.FlowNodeStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowNodesDao;
import com.everhomes.server.schema.tables.pojos.EhFlowNodes;
import com.everhomes.server.schema.tables.records.EhFlowNodesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowNodeProviderImpl implements FlowNodeProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowNode(FlowNode obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowNodes.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowNodesDao dao = new EhFlowNodesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowNode(FlowNode obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));
        EhFlowNodesDao dao = new EhFlowNodesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowNode(FlowNode obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));
        EhFlowNodesDao dao = new EhFlowNodesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowNode getFlowNodeById(Long id) {
        try {
        FlowNode[] result = new FlowNode[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));

        result[0] = context.select().from(Tables.EH_FLOW_NODES)
            .where(Tables.EH_FLOW_NODES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowNode.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowNode> queryFlowNodes(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));

        SelectQuery<EhFlowNodesRecord> query = context.selectQuery(Tables.EH_FLOW_NODES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_NODES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowNode> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowNode.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowNode obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }

	@Override
	public FlowNode findFlowNodeByName(Long flowMainId, Integer flowVersion, String nodeName) {
		ListingLocator locator = new ListingLocator();
		List<FlowNode> flowNodes = this.queryFlowNodes(locator, 1, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_NODES.FLOW_MAIN_ID.eq(flowMainId));
				query.addConditions(Tables.EH_FLOW_NODES.FLOW_VERSION.eq(flowVersion));
				query.addConditions(Tables.EH_FLOW_NODES.NODE_NAME.eq(nodeName));
				query.addConditions(Tables.EH_FLOW_NODES.STATUS.eq(FlowNodeStatus.VISIBLE.getCode()));
				return query;
			}
			
		});
		
		if(flowNodes == null || flowNodes.size() == 0) {
			return null;
		}
		
		return flowNodes.get(0);
	}
	
	@Override
	public List<FlowNode> findFlowNodesByFlowId(Long flowMainId, Integer flowVersion) {
		ListingLocator locator = new ListingLocator();
		List<FlowNode> flowNodes = this.queryFlowNodes(locator, 100, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_NODES.FLOW_MAIN_ID.eq(flowMainId));
				query.addConditions(Tables.EH_FLOW_NODES.FLOW_VERSION.eq(flowVersion));
				query.addConditions(Tables.EH_FLOW_NODES.STATUS.ne(FlowNodeStatus.INVALID.getCode()));
				return query;
			}
			
		});
		
		return flowNodes;
	}
}
