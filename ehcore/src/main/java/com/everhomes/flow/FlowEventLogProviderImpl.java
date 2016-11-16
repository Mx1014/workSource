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
import com.everhomes.server.schema.tables.daos.EhFlowEventLogsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.server.schema.tables.records.EhFlowEventLogsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowEventLogProviderImpl implements FlowEventLogProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowEventLog(FlowEventLog obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowEventLogs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowEventLog(FlowEventLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
        EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowEventLog(FlowEventLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
        EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowEventLog getFlowEventLogById(Long id) {
        try {
        FlowEventLog[] result = new FlowEventLog[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));

        result[0] = context.select().from(Tables.EH_FLOW_EVENT_LOGS)
            .where(Tables.EH_FLOW_EVENT_LOGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowEventLog.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowEventLog> queryFlowEventLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));

        SelectQuery<EhFlowEventLogsRecord> query = context.selectQuery(Tables.EH_FLOW_EVENT_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowEventLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowEventLog.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowEventLog obj) {
    }
}
