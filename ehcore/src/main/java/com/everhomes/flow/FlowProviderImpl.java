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
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowsDao;
import com.everhomes.server.schema.tables.pojos.EhFlows;
import com.everhomes.server.schema.tables.records.EhFlowsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowProviderImpl implements FlowProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlow(Flow obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlows.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlow(Flow obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlow(Flow obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public Flow getFlowById(Long id) {
        try {
        Flow[] result = new Flow[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));

        result[0] = context.select().from(Tables.EH_FLOWS)
            .where(Tables.EH_FLOWS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, Flow.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<Flow> queryFlows(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));

        SelectQuery<EhFlowsRecord> query = context.selectQuery(Tables.EH_FLOWS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOWS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<Flow> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, Flow.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(Flow obj) {
    }
}
