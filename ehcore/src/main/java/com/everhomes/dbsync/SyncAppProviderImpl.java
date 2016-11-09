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
import com.everhomes.server.schema.tables.daos.EhSyncAppsDao;
import com.everhomes.server.schema.tables.pojos.EhSyncApps;
import com.everhomes.server.schema.tables.records.EhSyncAppsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class SyncAppProviderImpl implements SyncAppProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createSyncApp(SyncApp obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSyncApps.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncApps.class));
        obj.setId(id);
        prepareObj(obj);
        EhSyncAppsDao dao = new EhSyncAppsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateSyncApp(SyncApp obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncApps.class));
        EhSyncAppsDao dao = new EhSyncAppsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteSyncApp(SyncApp obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncApps.class));
        EhSyncAppsDao dao = new EhSyncAppsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public SyncApp getSyncAppById(Long id) {
        try {
        SyncApp[] result = new SyncApp[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncApps.class));

        result[0] = context.select().from(Tables.EH_SYNC_APPS)
            .where(Tables.EH_SYNC_APPS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, SyncApp.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<SyncApp> querySyncApps(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncApps.class));

        SelectQuery<EhSyncAppsRecord> query = context.selectQuery(Tables.EH_SYNC_APPS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SYNC_APPS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<SyncApp> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, SyncApp.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(SyncApp obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
