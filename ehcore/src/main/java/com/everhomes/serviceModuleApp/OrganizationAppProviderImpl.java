package com.everhomes.serviceModuleApp;

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
//import com.everhomes.server.schema.tables.daos.EhOrganizationAppsDao;
import com.everhomes.server.schema.tables.pojos.EhOrganizationApps;
import com.everhomes.server.schema.tables.records.EhOrganizationAppsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class OrganizationAppProviderImpl implements OrganizationAppProvider {
    /*@Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createOrganizationApp(OrganizationApp obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationApps.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationApps.class));
        obj.setId(id);
        prepareObj(obj);
        EhOrganizationAppsDao dao = new EhOrganizationAppsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateOrganizationApp(OrganizationApp obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationApps.class));
        EhOrganizationAppsDao dao = new EhOrganizationAppsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteOrganizationApp(OrganizationApp obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationApps.class));
        EhOrganizationAppsDao dao = new EhOrganizationAppsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public OrganizationApp getOrganizationAppById(Long id) {
        try {
        OrganizationApp[] result = new OrganizationApp[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationApps.class));

        result[0] = context.select().from(Tables.EH_ORGANIZATION_APPS)
            .where(Tables.EH_ORGANIZATION_APPS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, OrganizationApp.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<OrganizationApp> queryOrganizationApps(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationApps.class));

        SelectQuery<EhOrganizationAppsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_APPS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ORGANIZATION_APPS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<OrganizationApp> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OrganizationApp.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(OrganizationApp obj) {
    } */
}
