package com.everhomes.launchpad;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhLaunchPadIndexsDao;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadIndexs;
import com.everhomes.server.schema.tables.records.EhLaunchPadIndexsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class LaunchPadIndexProviderImpl implements LaunchPadIndexProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createLaunchPadIndex(LaunchPadIndex obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLaunchPadIndexs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadIndexs.class));
        obj.setId(id);
        prepareObj(obj);
        EhLaunchPadIndexsDao dao = new EhLaunchPadIndexsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateLaunchPadIndex(LaunchPadIndex obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadIndexs.class));
        EhLaunchPadIndexsDao dao = new EhLaunchPadIndexsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteLaunchPadIndex(LaunchPadIndex obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadIndexs.class));
        EhLaunchPadIndexsDao dao = new EhLaunchPadIndexsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public LaunchPadIndex getLaunchPadIndexById(Long id) {
        try {
        LaunchPadIndex[] result = new LaunchPadIndex[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadIndexs.class));

        result[0] = context.select().from(Tables.EH_LAUNCH_PAD_INDEXS)
            .where(Tables.EH_LAUNCH_PAD_INDEXS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, LaunchPadIndex.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<LaunchPadIndex> queryLaunchPadIndexs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadIndexs.class));

        SelectQuery<EhLaunchPadIndexsRecord> query = context.selectQuery(Tables.EH_LAUNCH_PAD_INDEXS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_LAUNCH_PAD_INDEXS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<LaunchPadIndex> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, LaunchPadIndex.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(LaunchPadIndex obj) {
    }
}
