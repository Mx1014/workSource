package com.everhomes.promotion;

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

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhOpPromotionAssignedScopesDao;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionAssignedScopes;
import com.everhomes.server.schema.tables.records.EhOpPromotionAssignedScopesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class OpPromotionAssignedScopeProviderImpl implements OpPromotionAssignedScopeProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createOpPromotionAssignedScope(OpPromotionAssignedScope obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOpPromotionAssignedScopes.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionAssignedScopes.class));
        obj.setId(id);
        prepareObj(obj);
        EhOpPromotionAssignedScopesDao dao = new EhOpPromotionAssignedScopesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateOpPromotionAssignedScope(OpPromotionAssignedScope obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionAssignedScopes.class));
        EhOpPromotionAssignedScopesDao dao = new EhOpPromotionAssignedScopesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteOpPromotionAssignedScope(OpPromotionAssignedScope obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionAssignedScopes.class));
        EhOpPromotionAssignedScopesDao dao = new EhOpPromotionAssignedScopesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public OpPromotionAssignedScope getOpPromotionAssignedScopeById(Long id) {
        try {
        OpPromotionAssignedScope[] result = new OpPromotionAssignedScope[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionAssignedScopes.class));

        result[0] = context.select().from(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES)
            .where(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, OpPromotionAssignedScope.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<OpPromotionAssignedScope> queryOpPromotionAssignedScopes(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionAssignedScopes.class));

        SelectQuery<EhOpPromotionAssignedScopesRecord> query = context.selectQuery(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<OpPromotionAssignedScope> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OpPromotionAssignedScope.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(OpPromotionAssignedScope obj) {
    }
    
    @Override
    public List<OpPromotionAssignedScope> getOpPromotionScopeByPromotionId(Long promotionId) {
        ListingLocator locator = new ListingLocator();
        int count = 100;
        
        return this.queryOpPromotionAssignedScopes(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.PROMOTION_ID.eq(promotionId));
                return query;
            }
            
        });
    }
}

