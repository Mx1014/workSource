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

import com.everhomes.rest.promotion.OpPromotionOwnerType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhOpPromotionMessagesDao;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionMessages;
import com.everhomes.server.schema.tables.records.EhOpPromotionMessagesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class OpPromotionMessageProviderImpl implements OpPromotionMessageProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createOpPromotionMessage(OpPromotionMessage obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOpPromotionMessages.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionMessages.class));
        obj.setId(id);
        prepareObj(obj);
        EhOpPromotionMessagesDao dao = new EhOpPromotionMessagesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateOpPromotionMessage(OpPromotionMessage obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionMessages.class));
        EhOpPromotionMessagesDao dao = new EhOpPromotionMessagesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteOpPromotionMessage(OpPromotionMessage obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionMessages.class));
        EhOpPromotionMessagesDao dao = new EhOpPromotionMessagesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public OpPromotionMessage getOpPromotionMessageById(Long id) {
        try {
        OpPromotionMessage[] result = new OpPromotionMessage[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionMessages.class));

        result[0] = context.select().from(Tables.EH_OP_PROMOTION_MESSAGES)
            .where(Tables.EH_OP_PROMOTION_MESSAGES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, OpPromotionMessage.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<OpPromotionMessage> queryOpPromotionMessages(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOpPromotionMessages.class));

        SelectQuery<EhOpPromotionMessagesRecord> query = context.selectQuery(Tables.EH_OP_PROMOTION_MESSAGES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_OP_PROMOTION_MESSAGES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<OpPromotionMessage> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OpPromotionMessage.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(OpPromotionMessage obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    public OpPromotionMessage findTargetByPromotionId(Long userId, Long promotionId) {
        List<OpPromotionMessage> messages = queryOpPromotionMessages(new ListingLocator(), 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_OP_PROMOTION_MESSAGES.OWNER_ID.eq(promotionId));
                query.addConditions(Tables.EH_OP_PROMOTION_MESSAGES.OWNER_TYPE.eq(OpPromotionOwnerType.USER.getCode()));
                query.addConditions(Tables.EH_OP_PROMOTION_MESSAGES.TARGET_UID.eq(userId));
                return query;
            }
            
        });
        
        if(messages != null && messages.size() > 0) {
            return messages.get(0);
        }
        
        return null;
    }
}
