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
import com.everhomes.server.schema.tables.daos.EhFlowEvaluatesDao;
import com.everhomes.server.schema.tables.pojos.EhFlowEvaluates;
import com.everhomes.server.schema.tables.records.EhFlowEvaluatesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowEvaluateProviderImpl implements FlowEvaluateProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowEvaluate(FlowEvaluate obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowEvaluates.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluates.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowEvaluatesDao dao = new EhFlowEvaluatesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowEvaluate(FlowEvaluate obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluates.class));
        EhFlowEvaluatesDao dao = new EhFlowEvaluatesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowEvaluate(FlowEvaluate obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluates.class));
        EhFlowEvaluatesDao dao = new EhFlowEvaluatesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowEvaluate getFlowEvaluateById(Long id) {
        try {
        FlowEvaluate[] result = new FlowEvaluate[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluates.class));

        result[0] = context.select().from(Tables.EH_FLOW_EVALUATES)
            .where(Tables.EH_FLOW_EVALUATES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowEvaluate.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowEvaluate> queryFlowEvaluates(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEvaluates.class));

        SelectQuery<EhFlowEvaluatesRecord> query = context.selectQuery(Tables.EH_FLOW_EVALUATES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_EVALUATES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowEvaluate> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowEvaluate.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowEvaluate obj) {
    }
}
