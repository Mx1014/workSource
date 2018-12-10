package com.everhomes.aclink;

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

import com.everhomes.rest.aclink.AesUserKeyStatus;
import com.everhomes.rest.aclink.AesUserKeyType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAesUserKeyDao;
import com.everhomes.server.schema.tables.pojos.EhAesUserKey;
import com.everhomes.server.schema.tables.records.EhAesUserKeyRecord;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class AesUserKeyProviderImpl implements AesUserKeyProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createAesUserKey(AesUserKey obj) {
        Long id = obj.getId();
        if(id == null || id <= 0) {
            id = prepareForAesUserKeyId();
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        obj.setId(id);
        prepareObj(obj);
        EhAesUserKeyDao dao = new EhAesUserKeyDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAesUserKey(AesUserKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAesUserKeyDao dao = new EhAesUserKeyDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAesUserKey(AesUserKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAesUserKeyDao dao = new EhAesUserKeyDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public AesUserKey getAesUserKeyById(Long id) {
        try {
        AesUserKey[] result = new AesUserKey[1];

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhDoorAccess.class), null,
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_AES_USER_KEY)
                    .where(Tables.EH_AES_USER_KEY.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, AesUserKey.class);
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
    public List<AesUserKey> queryAesUserKeyByDoorId(ListingLocator locator, Long refId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDoorAccess.class, refId));

        SelectQuery<EhAesUserKeyRecord> query = context.selectQuery(Tables.EH_AES_USER_KEY);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        query.addConditions(Tables.EH_AES_USER_KEY.DOOR_ID.eq(refId));

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_AES_USER_KEY.ID.lt(locator.getAnchor()));
            }

        query.addLimit(count);
        query.addOrderBy(Tables.EH_AES_USER_KEY.ID.desc());
        List<AesUserKey> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AesUserKey.class);
        });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }

    @Override
    public List<AesUserKey> queryAesUserKeys(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<AesUserKey> objs = new ArrayList<AesUserKey>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhAesUserKeyRecord> query = context.selectQuery(Tables.EH_AES_USER_KEY);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_AES_USER_KEY.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_AES_USER_KEY.ID.asc());
            query.addLimit(count - objs.size());

            query.fetch().map((r) -> {
                objs.add(ConvertHelper.convert(r, AesUserKey.class));
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

    private void prepareObj(AesUserKey obj) {
//        int keyId = (int) (obj.getId().intValue() % MAX_KEY_ID);
//        obj.setKeyId(new Integer(keyId));
//        obj.setSecret(AclinkUtils);
        obj.setCreateTimeMs(System.currentTimeMillis());
    }
    
    @Override
    public Long prepareForAesUserKeyId() {
        return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAesUserKey.class));
    }
    
    @Override
    public AesUserKey queryAesUserKeyByDoorId(Long doorId, Long userId) {
        return queryAesUserKeyByDoorId(doorId, userId, -1l);
    }
    
    @Override
    public AesUserKey queryAesUserKeyByDoorId(Long doorId, Long userId, Long ignoreAuthId) {
        ListingLocator locator = new ListingLocator();
        
        long now = DateHelper.currentGMTTime().getTime() + 10*60*1000l;
        
        List<AesUserKey> aesUserKeys = queryAesUserKeyByDoorId(locator, doorId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_AES_USER_KEY.USER_ID.eq(userId));
                query.addConditions(Tables.EH_AES_USER_KEY.EXPIRE_TIME_MS.ge(now));
                query.addConditions(Tables.EH_AES_USER_KEY.STATUS.eq(AesUserKeyStatus.VALID.getCode()));
                query.addConditions(Tables.EH_AES_USER_KEY.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_AES_USER_KEY.AUTH_ID.ne(ignoreAuthId));
                return query;
            }
            
        });
        
        if(aesUserKeys.size() > 0) {
            return aesUserKeys.get(0);
        }
        
        return null;
    }
    
    @Override
    public AesUserKey queryAesUserKeyByAuthId(Long doorId, Long authId) {
        ListingLocator locator = new ListingLocator();
        
        List<AesUserKey> aesUserKeys = queryAesUserKeyByDoorId(locator, doorId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_AES_USER_KEY.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_AES_USER_KEY.AUTH_ID.eq(authId));
                return query;
            }
            
        });
        
        if(aesUserKeys.size() > 0) {
            return aesUserKeys.get(0);
        }
        
        return null;
    }
    
    @Override
    public List<AesUserKey> queryAdminAesUserKeyByUserId(Long userId, int maxCount) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<AesUserKey> keys = queryAesUserKeys(locator, maxCount,
                new ListingQueryBuilderCallback() {

                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                            SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_AES_USER_KEY.KEY_TYPE.ne(AesUserKeyType.TEMP.getCode()));
                        query.addConditions(Tables.EH_AES_USER_KEY.USER_ID.eq(userId));
                        return query;
                    }
            
        });
        
        return keys;
    }
    
}
