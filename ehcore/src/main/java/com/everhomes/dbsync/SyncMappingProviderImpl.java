package com.everhomes.dbsync;

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
import com.everhomes.server.schema.tables.daos.EhSyncMappingDao;
import com.everhomes.server.schema.tables.pojos.EhSyncMapping;
import com.everhomes.server.schema.tables.records.EhSyncMappingRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class SyncMappingProviderImpl implements SyncMappingProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createSyncMapping(SyncMapping obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSyncMapping.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncMapping.class));
        obj.setId(id);
        prepareObj(obj);
        EhSyncMappingDao dao = new EhSyncMappingDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateSyncMapping(SyncMapping obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncMapping.class));
        EhSyncMappingDao dao = new EhSyncMappingDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteSyncMapping(SyncMapping obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncMapping.class));
        EhSyncMappingDao dao = new EhSyncMappingDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public SyncMapping getSyncMappingById(Long id) {
        try {
        SyncMapping[] result = new SyncMapping[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncMapping.class));

        result[0] = context.select().from(Tables.EH_SYNC_MAPPING)
            .where(Tables.EH_SYNC_MAPPING.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, SyncMapping.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<SyncMapping> querySyncMappings(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncMapping.class));

        SelectQuery<EhSyncMappingRecord> query = context.selectQuery(Tables.EH_SYNC_MAPPING);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SYNC_MAPPING.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<SyncMapping> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, SyncMapping.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(SyncMapping obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
