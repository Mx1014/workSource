package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.server.schema.tables.daos.EhAppCommunityConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhAppCommunityConfigs;
import com.everhomes.server.schema.tables.records.EhAppCommunityConfigsRecord;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
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
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAppCommunityConfigs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));
        obj.setId(id);
        prepareObj(obj);
        EhAppCommunityConfigsDao dao = new EhAppCommunityConfigsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAppCommunityConfig(AppCommunityConfig obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));
        EhAppCommunityConfigsDao dao = new EhAppCommunityConfigsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAppCommunityConfig(AppCommunityConfig obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));
        EhAppCommunityConfigsDao dao = new EhAppCommunityConfigsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public void deleteAppCommunityConfigByCommunityId(Long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));
        DeleteQuery<EhAppCommunityConfigsRecord> query = context.deleteQuery(Tables.EH_APP_COMMUNITY_CONFIGS);
        query.addConditions(Tables.EH_APP_COMMUNITY_CONFIGS.COMMUNITY_ID.eq(communityId));
        query.execute();
    }

    @Override
    public void deleteAppCommunityConfigByCommunityIdAndAppOriginId(Long communityId, Long AppOriginId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));
        DeleteQuery<EhAppCommunityConfigsRecord> query = context.deleteQuery(Tables.EH_APP_COMMUNITY_CONFIGS);
        query.addConditions(Tables.EH_APP_COMMUNITY_CONFIGS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_APP_COMMUNITY_CONFIGS.APP_ORIGIN_ID.eq(AppOriginId));
        query.execute();
    }

    @Override
    public AppCommunityConfig getAppCommunityConfigById(Long id) {
        try {
        AppCommunityConfig[] result = new AppCommunityConfig[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));

        result[0] = context.select().from(Tables.EH_APP_COMMUNITY_CONFIGS)
            .where(Tables.EH_APP_COMMUNITY_CONFIGS.ID.eq(id))
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
    public AppCommunityConfig findAppCommunityConfigByCommunityIdAndAppOriginId(Long communityId, Long appOriginId) {
        try {
            AppCommunityConfig[] result = new AppCommunityConfig[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));

            result[0] = context.select().from(Tables.EH_APP_COMMUNITY_CONFIGS)
                    .where(Tables.EH_APP_COMMUNITY_CONFIGS.COMMUNITY_ID.eq(communityId)
                            .and(Tables.EH_APP_COMMUNITY_CONFIGS.APP_ORIGIN_ID.eq(appOriginId)))
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
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAppCommunityConfigs.class));

        SelectQuery<EhAppCommunityConfigsRecord> query = context.selectQuery(Tables.EH_APP_COMMUNITY_CONFIGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_APP_COMMUNITY_CONFIGS.ID.gt(locator.getAnchor()));
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
