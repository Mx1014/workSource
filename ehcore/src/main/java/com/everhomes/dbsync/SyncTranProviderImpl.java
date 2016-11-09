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
import com.everhomes.server.schema.tables.daos.EhSyncTransDao;
import com.everhomes.server.schema.tables.pojos.EhSyncTrans;
import com.everhomes.server.schema.tables.records.EhSyncTransRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class SyncTranProviderImpl implements SyncTranProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createSyncTran(SyncTran obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSyncTrans.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncTrans.class));
        obj.setId(id);
        prepareObj(obj);
        EhSyncTransDao dao = new EhSyncTransDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateSyncTran(SyncTran obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncTrans.class));
        EhSyncTransDao dao = new EhSyncTransDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteSyncTran(SyncTran obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncTrans.class));
        EhSyncTransDao dao = new EhSyncTransDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public SyncTran getSyncTranById(Long id) {
        try {
        SyncTran[] result = new SyncTran[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncTrans.class));

        result[0] = context.select().from(Tables.EH_SYNC_TRANS)
            .where(Tables.EH_SYNC_TRANS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, SyncTran.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<SyncTran> querySyncTrans(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncTrans.class));

        SelectQuery<EhSyncTransRecord> query = context.selectQuery(Tables.EH_SYNC_TRANS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SYNC_TRANS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<SyncTran> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, SyncTran.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(SyncTran obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
