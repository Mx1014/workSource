package com.everhomes.acl;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppAuthorizationsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAppAuthorizations;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppAuthorizationsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RecordHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceModuleAppAuthorizationProviderImpl implements ServiceModuleAppAuthorizationProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAppAuthorizations.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppAuthorizations.class));
        obj.setId(id);
        prepareObj(obj);
        EhServiceModuleAppAuthorizationsDao dao = new EhServiceModuleAppAuthorizationsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public Long createServiceModuleAppAuthorizations(List<ServiceModuleAppAuthorization> objs) {
        long id = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAppAuthorizations.class), (long)objs.size());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppAuthorizations.class));
        List<EhServiceModuleAppAuthorizations> authorizations = new ArrayList<>();
        for(ServiceModuleAppAuthorization r: objs){
            r.setId(id);
            prepareObj(r);
            authorizations.add(ConvertHelper.convert(r, EhServiceModuleAppAuthorizations.class));
            id++;
        }
        EhServiceModuleAppAuthorizationsDao dao = new EhServiceModuleAppAuthorizationsDao(context.configuration());
        dao.insert(authorizations);
        return id;
    }

    @Override
    public void updateServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppAuthorizations.class));
        EhServiceModuleAppAuthorizationsDao dao = new EhServiceModuleAppAuthorizationsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void updateServiceModuleAppAuthorizations(List<ServiceModuleAppAuthorization> objs) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppAuthorizations.class));
        EhServiceModuleAppAuthorizationsDao dao = new EhServiceModuleAppAuthorizationsDao(context.configuration());
        dao.update(objs.stream().map(r-> ConvertHelper.convert(r, EhServiceModuleAppAuthorizations.class)).collect(Collectors.toList()));
    }

    @Override
    public void deleteServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppAuthorizations.class));
        EhServiceModuleAppAuthorizationsDao dao = new EhServiceModuleAppAuthorizationsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ServiceModuleAppAuthorization getServiceModuleAppAuthorizationById(Long id) {
        try {
        ServiceModuleAppAuthorization[] result = new ServiceModuleAppAuthorization[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppAuthorizations.class));

        result[0] = context.select().from(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS)
            .where(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ServiceModuleAppAuthorization.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ServiceModuleAppAuthorization> queryServiceModuleAppAuthorizations(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppAuthorizations.class));

        SelectQuery<Record> query = context.select(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.fields()).from(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS).getQuery();
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ServiceModuleAppAuthorization> objs = query.fetch().map((r) -> {
            return RecordHelper.convert(r, ServiceModuleAppAuthorization.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(ServiceModuleAppAuthorization obj) {
    }
}
