package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAppCommunityConfigDao;
import com.everhomes.server.schema.tables.pojos.EhAppCommunityConfig;
import com.everhomes.server.schema.tables.records.EhAppCommunityConfigRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AppCommunityConfigProviderImpl implements AppCommunityConfigProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createAppCommunityConfig(AppCommunityConfig obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAppCommunityConfig.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfig.class));
        obj.setId(id);
        prepareObj(obj);
        EhAppCommunityConfigDao dao = new EhAppCommunityConfigDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAppCommunityConfig(AppCommunityConfig obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfig.class));
        EhAppCommunityConfigDao dao = new EhAppCommunityConfigDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAppCommunityConfig(AppCommunityConfig obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfig.class));
        EhAppCommunityConfigDao dao = new EhAppCommunityConfigDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public AppCommunityConfig getAppCommunityConfigById(Long id) {
        try {
        AppCommunityConfig[] result = new AppCommunityConfig[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfig.class));

        result[0] = context.select().from(Tables.EH_APP_COMMUNITY_CONFIG)
            .where(Tables.EH_APP_COMMUNITY_CONFIG.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, AppCommunityConfig.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<AppCommunityConfig> queryAppCommunityConfigs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfig.class));

        SelectQuery<EhAppCommunityConfigRecord> query = context.selectQuery(Tables.EH_APP_COMMUNITY_CONFIG);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_APP_COMMUNITY_CONFIG.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<AppCommunityConfig> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AppCommunityConfig.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(AppCommunityConfig obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
