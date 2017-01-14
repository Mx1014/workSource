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
import com.everhomes.server.schema.tables.daos.EhFlowFormsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowForms;
import com.everhomes.server.schema.tables.records.EhFlowFormsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowFormProviderImpl implements FlowFormProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowForm(FlowForm obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowForms.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowForms.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowFormsDao dao = new EhFlowFormsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowForm(FlowForm obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowForms.class));
        EhFlowFormsDao dao = new EhFlowFormsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowForm(FlowForm obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowForms.class));
        EhFlowFormsDao dao = new EhFlowFormsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowForm getFlowFormById(Long id) {
        try {
        FlowForm[] result = new FlowForm[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowForms.class));

        result[0] = context.select().from(Tables.EH_FLOW_FORMS)
            .where(Tables.EH_FLOW_FORMS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowForm.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowForm> queryFlowForms(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowForms.class));

        SelectQuery<EhFlowFormsRecord> query = context.selectQuery(Tables.EH_FLOW_FORMS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_FORMS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowForm> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowForm.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowForm obj) {
    }
}
