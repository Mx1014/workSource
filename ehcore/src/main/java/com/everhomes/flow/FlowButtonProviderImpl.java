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
import com.everhomes.server.schema.tables.daos.EhFlowButtonsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowButtons;
import com.everhomes.server.schema.tables.records.EhFlowButtonsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowButtonProviderImpl implements FlowButtonProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowButton(FlowButton obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowButtons.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowButtons.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowButtonsDao dao = new EhFlowButtonsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowButton(FlowButton obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowButtons.class));
        EhFlowButtonsDao dao = new EhFlowButtonsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowButton(FlowButton obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowButtons.class));
        EhFlowButtonsDao dao = new EhFlowButtonsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowButton getFlowButtonById(Long id) {
        try {
        FlowButton[] result = new FlowButton[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowButtons.class));

        result[0] = context.select().from(Tables.EH_FLOW_BUTTONS)
            .where(Tables.EH_FLOW_BUTTONS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowButton.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowButton> queryFlowButtons(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowButtons.class));

        SelectQuery<EhFlowButtonsRecord> query = context.selectQuery(Tables.EH_FLOW_BUTTONS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_BUTTONS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowButton> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowButton.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowButton obj) {
    }
}
