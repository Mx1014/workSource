package com.everhomes.pushmessage;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPushMessagesDao;
import com.everhomes.server.schema.tables.records.EhPushMessagesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PushMessageProviderImpl implements PushMessageProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    /**
     * 是独立的一张主表
     * @param pushMessage
     */
    @Override
    @CacheEvict(value = "PushMessage", key="#pushMessage.id")
    public void createPushMessage(PushMessage pushMessage) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        InsertQuery<EhPushMessagesRecord> query = context.insertQuery(Tables.EH_PUSH_MESSAGES);
        pushMessage.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        query.setRecord(ConvertHelper.convert(pushMessage, EhPushMessagesRecord.class));
        query.setReturning(Tables.EH_PUSH_MESSAGES.ID);
        if(query.execute() > 0) {
            pushMessage.setId(query.getReturnedRecord().getId());
        }
    }
    
    @Override
    @CacheEvict(value = "PushMessage", key="#id")
    public void deleteByPushMesageId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPushMessagesDao dao = new EhPushMessagesDao(context.configuration());
        dao.deleteById(id);
    }
    
    @Override
    @CacheEvict(value = "PushMessage", key="#pushMessage.id")
    public void updatePushMessage(PushMessage pushMessage) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhPushMessagesDao dao = new EhPushMessagesDao(context.configuration());
        dao.update(pushMessage);
    }
    
    @Override
    @Cacheable(value="PushMessage", key="#id", unless="#result == null")
    public PushMessage getPushMessageById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPushMessagesDao dao = new EhPushMessagesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), PushMessage.class);
    }
    
    @Override
    public List<PushMessage> queryPushMessages(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPushMessagesRecord> query = context.selectQuery(Tables.EH_PUSH_MESSAGES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PUSH_MESSAGES.ID.gt(locator.getAnchor()));
            }
        query.addOrderBy(Tables.EH_PUSH_MESSAGES.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PushMessage.class);
        });
    }
    
    @Override
    public List<PushMessage> queryPushMessages(ListingLocator locator, int count) {
        return queryPushMessages(locator, count, null);
    }
}
