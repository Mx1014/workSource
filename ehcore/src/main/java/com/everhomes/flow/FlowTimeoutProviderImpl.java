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
import com.everhomes.server.schema.tables.daos.EhFlowTimeoutsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowTimeouts;
import com.everhomes.server.schema.tables.records.EhFlowTimeoutsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class FlowTimeoutProviderImpl implements FlowTimeoutProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowTimeout(FlowTimeout obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowTimeouts.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowTimeouts.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowTimeoutsDao dao = new EhFlowTimeoutsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowTimeout(FlowTimeout obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowTimeouts.class));
        EhFlowTimeoutsDao dao = new EhFlowTimeoutsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowTimeout(FlowTimeout obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowTimeouts.class));
        EhFlowTimeoutsDao dao = new EhFlowTimeoutsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowTimeout getFlowTimeoutById(Long id) {
        try {
        FlowTimeout[] result = new FlowTimeout[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowTimeouts.class));

        result[0] = context.select().from(Tables.EH_FLOW_TIMEOUTS)
            .where(Tables.EH_FLOW_TIMEOUTS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowTimeout.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowTimeout> queryFlowTimeouts(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowTimeouts.class));

        SelectQuery<EhFlowTimeoutsRecord> query = context.selectQuery(Tables.EH_FLOW_TIMEOUTS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_TIMEOUTS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowTimeout> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowTimeout.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowTimeout obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    public boolean deleteIfValid(Long timeoutId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowTimeouts.class));
        
        int effect = context.update(Tables.EH_FLOW_TIMEOUTS)
        .set(Tables.EH_FLOW_TIMEOUTS.STATUS, FlowStatusType.INVALID.getCode())
        .where(Tables.EH_FLOW_TIMEOUTS.STATUS.eq(FlowStatusType.VALID.getCode()).and(Tables.EH_FLOW_TIMEOUTS.ID.eq(timeoutId)))
        .execute();
        
        if(effect > 0) {
        	return true;
        }
        
        return false;
    }
    
    @Override
    public List<FlowTimeout> queryValids(ListingLocator locator, int count) {
    	return queryFlowTimeouts(locator, count, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_TIMEOUTS.STATUS.eq(FlowStatusType.VALID.getCode()));
				return query;
			}
    	});
    }
}
