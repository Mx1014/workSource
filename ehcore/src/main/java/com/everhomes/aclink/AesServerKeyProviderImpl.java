package com.everhomes.aclink;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAesServerKeyDao;
import com.everhomes.server.schema.tables.pojos.EhAesServerKey;
import com.everhomes.server.schema.tables.records.EhAesServerKeyRecord;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class AesServerKeyProviderImpl implements AesServerKeyProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(AesServerKeyProvider.class);
    
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createAesServerKey(AesServerKey obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAesServerKey.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        obj.setId(id);
        prepareObj(obj);
        EhAesServerKeyDao dao = new EhAesServerKeyDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAesServerKey(AesServerKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAesServerKeyDao dao = new EhAesServerKeyDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAesServerKey(AesServerKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAesServerKeyDao dao = new EhAesServerKeyDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public AesServerKey getAesServerKeyById(Long id) {
        try {
        AesServerKey[] result = new AesServerKey[1];

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhDoorAccess.class), null,
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_AES_SERVER_KEY)
                    .where(Tables.EH_AES_SERVER_KEY.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, AesServerKey.class);
                    });

                if (result[0] != null) {
                    return false;
                } else {
                    return true;
                }
            });

        return result[0];
    } catch (Exception ex) {
        //TODO fetchAny() maybe return null
        return null;
    }
    }

    @Override
    public List<AesServerKey> queryAesServerKeyByDoorId(ListingLocator locator, Long refId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDoorAccess.class, refId));

        SelectQuery<EhAesServerKeyRecord> query = context.selectQuery(Tables.EH_AES_SERVER_KEY);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        query.addConditions(Tables.EH_AES_SERVER_KEY.DOOR_ID.eq(refId));

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_AES_SERVER_KEY.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<AesServerKey> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AesServerKey.class);
        });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }

    @Override
    public List<AesServerKey> queryAesServerKeys(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<AesServerKey> objs = new ArrayList<AesServerKey>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhAesServerKeyRecord> query = context.selectQuery(Tables.EH_AES_SERVER_KEY);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_AES_SERVER_KEY.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_AES_SERVER_KEY.ID.desc());//always got the last one aes server key
            query.addLimit(count - objs.size());

            query.fetch().map((r) -> {
                objs.add(ConvertHelper.convert(r, AesServerKey.class));
                return null;
            });

            if(objs.size() >= count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;

        });
        return objs;
    }
    
    private void prepareObj(AesServerKey obj) {
    }

    @Override
    public AesServerKey queryAesServerKeyByDoorId(Long doorId, Long ver) {
        ListingLocator locator = new ListingLocator();
        List<AesServerKey> serverKeys = this.queryAesServerKeyByDoorId(locator, doorId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_AES_SERVER_KEY.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_AES_SERVER_KEY.SECRET_VER.eq(ver));
                return query;
            }
            
        });
        
        if( serverKeys == null || serverKeys.size() == 0) {
        return null;
        }
        
        return serverKeys.get(0);
    }
}

