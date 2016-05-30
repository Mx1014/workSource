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

import com.everhomes.rest.aclink.DoorAccessStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAclinksDao;
import com.everhomes.server.schema.tables.pojos.EhAclinks;
import com.everhomes.server.schema.tables.records.EhAclinksRecord;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class AclinkProviderImpl implements AclinkProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createAclink(Aclink obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinks.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        obj.setId(id);
        prepareObj(obj);
        EhAclinksDao dao = new EhAclinksDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAclink(Aclink obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAclinksDao dao = new EhAclinksDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAclink(Aclink obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getDoorId()));
        EhAclinksDao dao = new EhAclinksDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public Aclink getAclinkById(Long id) {
        Aclink[] result = new Aclink[1];

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhDoorAccess.class), null,
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_ACLINKS)
                    .where(Tables.EH_ACLINKS.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, Aclink.class);
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
    public List<Aclink> queryAclinkByDoorId(ListingLocator locator, Long refId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDoorAccess.class, refId));

        SelectQuery<EhAclinksRecord> query = context.selectQuery(Tables.EH_ACLINKS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        query.addConditions(Tables.EH_ACLINKS.DOOR_ID.eq(refId));

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINKS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<Aclink> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, Aclink.class);
        });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        }
        
        return objs;
    }

    @Override
    public List<Aclink> queryAclinks(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<Aclink> objs = new ArrayList<Aclink>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhAclinksRecord> query = context.selectQuery(Tables.EH_ACLINKS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_ACLINKS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ACLINKS.ID.asc());
            query.addLimit(count - objs.size());

            query.fetch().map((r) -> {
                objs.add(ConvertHelper.convert(r, Aclink.class));
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

    private void prepareObj(Aclink obj) {
    }
    
    @Override
    public Aclink getAclinkByDoorId(Long doorId) {
        ListingLocator locator = new ListingLocator();
        List<Aclink> aclinks = this.queryAclinkByDoorId(locator, doorId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ACLINKS.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_ACLINKS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
                return query;
            }
            
        });
        
        if(aclinks != null && aclinks.size() > 0) {
            return aclinks.get(0);
        }
        
        return null;
    }
}
