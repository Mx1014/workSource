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

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhDoorAccessDao;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.server.schema.tables.records.EhDoorAccessRecord;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class DoorAccessProviderImpl implements DoorAccessProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDoorAccess(DoorAccess obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorAccess.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getId()));
        obj.setId(id);
        prepareObj(obj);
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateDoorAccess(DoorAccess obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getId()));
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteDoorAccess(DoorAccess obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getId()));
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public DoorAccess getDoorAccessById(Long id) {
        DoorAccess[] result = new DoorAccess[1];

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhDoorAccess.class), null,
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_DOOR_ACCESS)
                    .where(Tables.EH_DOOR_ACCESS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, DoorAccess.class);
                    });

                if (result[0] != null) {
                    return false;
                } else {
                    return true;
                }
            });

        return result[0];
    }

    @Override
    public List<DoorAccess> queryDoorAccesss(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<DoorAccess> objs = new ArrayList<DoorAccess>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhDoorAccessRecord> query = context.selectQuery(Tables.EH_DOOR_ACCESS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_DOOR_ACCESS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_DOOR_ACCESS.ID.asc());
            query.addLimit(count - objs.size());

            query.fetch().map((r) -> {
                objs.add(ConvertHelper.convert(r, DoorAccess.class));
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

    private void prepareObj(DoorAccess obj) {
    }
    
    @Override
    public List<DoorAccess> listDoorAccessByCommunityId(Long communityId, CrossShardListingLocator locator, int count) {
        locator.setEntityId(communityId);
        
        return this.queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(communityId));
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(DoorAccessOwnerType.COMMUNITY.getCode()));
                return query;
            }
            
        });
    }
    
    @Override
    public List<DoorAccess> listDoorAccessByEnterpriseId(Long enterpriseId, CrossShardListingLocator locator, int count) {
        locator.setEntityId(enterpriseId);
        
        return this.queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(enterpriseId));
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(DoorAccessOwnerType.ENTERPRISE.getCode()));
                return query;
            }
            
        });
        
    }
}
