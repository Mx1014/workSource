package com.everhomes.pushmessage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPushMessageResultsDao;
import com.everhomes.server.schema.tables.pojos.EhPushMessageResults;
import com.everhomes.server.schema.tables.records.EhPushMessageResultsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class PushMessageResultProviderImpl implements PushMessageResultProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    /**
     * 推送结果是独立的一个分区
     * @param pushMessageResult
     */
    @Override
    public void createPushMessageResult(PushMessageResult pushMessageResult) {
        long id = this.shardingProvider.allocShardableContentId(EhPushMessageResults.class).second();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPushMessageResults.class, id));
        pushMessageResult.setId(id);
        pushMessageResult.setSendTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhPushMessageResultsDao dao = new EhPushMessageResultsDao(context.configuration());
        dao.insert(pushMessageResult);
    }
    
    @Override
    public void updatePushMessageResult(PushMessageResult pushMessageResult) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPushMessageResults.class, pushMessageResult.getId()));
        
        UpdateQuery<EhPushMessageResultsRecord> item = context.updateQuery(Tables.EH_PUSH_MESSAGE_RESULTS);
        item.setRecord(ConvertHelper.convert(pushMessageResult, EhPushMessageResultsRecord.class));
        item.setReturning(Tables.EH_PUSH_MESSAGE_RESULTS.ID);
        item.execute();
    }
    
    /**
     * TODO for cache
     * @param id
     * @return
     */
    @Override
    public PushMessageResult getPushMessageResultById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPushMessageResults.class, id));
        EhPushMessageResultsDao dao = new EhPushMessageResultsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), PushMessageResult.class);
    }
    
    @Override
    public List<PushMessageResult> queryPushMessageResult(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        
        final List<PushMessageResult> pushMessageResults = new ArrayList<PushMessageResult>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhPushMessageResults.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhPushMessageResultsRecord> query = context.selectQuery(Tables.EH_PUSH_MESSAGE_RESULTS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_PUSH_MESSAGE_RESULTS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_PUSH_MESSAGE_RESULTS.ID.asc());
            query.addLimit(count - pushMessageResults.size());
            
            query.fetch().map((r) -> {
                pushMessageResults.add(ConvertHelper.convert(r, PushMessageResult.class));
                return null;
            });
           
            if(pushMessageResults.size() >= count) {
                locator.setAnchor(pushMessageResults.get(pushMessageResults.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
 
        });
        return pushMessageResults;
    }
    
    @Override
    public List<PushMessageResult> queryPushMessageResultByIdentify(CrossShardListingLocator locator, int count, Long targetUserId) {
        return this.queryPushMessageResult(locator, count, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(targetUserId != null) {
                    query.addConditions(Tables.EH_PUSH_MESSAGE_RESULTS.USER_ID.eq(targetUserId));    
                    }
                return query;
            }
            
        });
    }
}
