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
import com.everhomes.server.schema.tables.daos.EhFlowStatsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowStats;
import com.everhomes.server.schema.tables.records.EhFlowStatsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowStatProviderImpl implements FlowStatProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowStat(FlowStat obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowStats.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowStats.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowStatsDao dao = new EhFlowStatsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowStat(FlowStat obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowStats.class));
        EhFlowStatsDao dao = new EhFlowStatsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowStat(FlowStat obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowStats.class));
        EhFlowStatsDao dao = new EhFlowStatsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowStat getFlowStatById(Long id) {
        try {
        FlowStat[] result = new FlowStat[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowStats.class));

        result[0] = context.select().from(Tables.EH_FLOW_STATS)
            .where(Tables.EH_FLOW_STATS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowStat.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowStat> queryFlowStats(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowStats.class));

        SelectQuery<EhFlowStatsRecord> query = context.selectQuery(Tables.EH_FLOW_STATS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_STATS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowStat> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowStat.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowStat obj) {
    }
}
