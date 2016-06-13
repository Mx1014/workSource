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
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAclinkUndoKeyDao;
import com.everhomes.server.schema.tables.pojos.EhAclinkUndoKey;
import com.everhomes.server.schema.tables.records.EhAclinkUndoKeyRecord;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class AclinkUndoKeyProviderImpl implements AclinkUndoKeyProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createAclinkUndoKey(AclinkUndoKey obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkUndoKey.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        obj.setId(id);
        prepareObj(obj);
        EhAclinkUndoKeyDao dao = new EhAclinkUndoKeyDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAclinkUndoKey(AclinkUndoKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAclinkUndoKeyDao dao = new EhAclinkUndoKeyDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAclinkUndoKey(AclinkUndoKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAclinkUndoKeyDao dao = new EhAclinkUndoKeyDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public AclinkUndoKey getAclinkUndoKeyById(Long id) {
        try {
        AclinkUndoKey[] result = new AclinkUndoKey[1];

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhDoorAccess.class), null,
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ACLINK_UNDO_KEY)
                    .where(Tables.EH_ACLINK_UNDO_KEY.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, AclinkUndoKey.class);
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
    public List<AclinkUndoKey> queryAclinkUndoKeyByDoorId(ListingLocator locator, Long refId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDoorAccess.class, refId));

        SelectQuery<EhAclinkUndoKeyRecord> query = context.selectQuery(Tables.EH_ACLINK_UNDO_KEY);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        query.addConditions(Tables.EH_ACLINK_UNDO_KEY.DOOR_ID.eq(refId));

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_UNDO_KEY.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<AclinkUndoKey> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkUndoKey.class);
        });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }

    @Override
    public List<AclinkUndoKey> queryAclinkUndoKeys(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<AclinkUndoKey> objs = new ArrayList<AclinkUndoKey>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhAclinkUndoKeyRecord> query = context.selectQuery(Tables.EH_ACLINK_UNDO_KEY);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ACLINK_UNDO_KEY.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ACLINK_UNDO_KEY.ID.asc());
            query.addLimit(count - objs.size());

            query.fetch().map((r) -> {
                objs.add(ConvertHelper.convert(r, AclinkUndoKey.class));
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

    private void prepareObj(AclinkUndoKey obj) {
    }
}
