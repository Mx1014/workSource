package com.everhomes.acl;

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
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppProfileDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAppProfile;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppProfileRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class ServiceModuleAppProfileProviderImpl implements ServiceModuleAppProfileProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createServiceModuleAppProfile(ServiceModuleAppProfile obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAppProfile.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppProfile.class));
        obj.setId(id);
        prepareObj(obj);
        EhServiceModuleAppProfileDao dao = new EhServiceModuleAppProfileDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateServiceModuleAppProfile(ServiceModuleAppProfile obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppProfile.class));
        EhServiceModuleAppProfileDao dao = new EhServiceModuleAppProfileDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteServiceModuleAppProfile(ServiceModuleAppProfile obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppProfile.class));
        EhServiceModuleAppProfileDao dao = new EhServiceModuleAppProfileDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ServiceModuleAppProfile getServiceModuleAppProfileById(Long id) {
        try {
        ServiceModuleAppProfile[] result = new ServiceModuleAppProfile[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppProfile.class));

        result[0] = context.select().from(Tables.EH_SERVICE_MODULE_APP_PROFILE)
            .where(Tables.EH_SERVICE_MODULE_APP_PROFILE.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ServiceModuleAppProfile.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public ServiceModuleAppProfile findServiceModuleAppProfileByOriginId(Long originId) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppProfile.class));
        SelectQuery<EhServiceModuleAppProfileRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_APP_PROFILE);
        query.addConditions(Tables.EH_SERVICE_MODULE_APP_PROFILE.ORIGIN_ID.eq(originId));
        return query.fetchAnyInto(ServiceModuleAppProfile.class);
    }

    @Override
    public List<ServiceModuleAppProfile> queryServiceModuleAppProfiles(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppProfile.class));

        SelectQuery<EhServiceModuleAppProfileRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_APP_PROFILE);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_PROFILE.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ServiceModuleAppProfile> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ServiceModuleAppProfile.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(ServiceModuleAppProfile obj) {
    }
}
