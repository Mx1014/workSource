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

import com.everhomes.rest.servicemoduleapp.OrganizationAppStatus;
import com.everhomes.util.RecordHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhOrganizationAppsDao;
import com.everhomes.server.schema.tables.pojos.EhOrganizationApps;
import com.everhomes.server.schema.tables.records.EhOrganizationAppsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class OrganizationAppProviderImpl implements OrganizationAppProvider {
    @Autowired
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

        SelectQuery<Record> query = context.select(Tables.EH_ORGANIZATION_APPS.fields()).from(Tables.EH_ORGANIZATION_APPS).getQuery();
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ORGANIZATION_APPS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<OrganizationApp> objs = query.fetch().map((r) -> {
            return RecordHelper.convert(r, OrganizationApp.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public OrganizationApp findOrganizationAppsByOriginIdAndOrgId(Long appOriginId, Long orgId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationApps.class));

        SelectQuery<EhOrganizationAppsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_APPS);

        query.addConditions(Tables.EH_ORGANIZATION_APPS.APP_ORIGIN_ID.eq(appOriginId));
        query.addConditions(Tables.EH_ORGANIZATION_APPS.ORG_ID.eq(orgId));
        query.addConditions(Tables.EH_ORGANIZATION_APPS.STATUS.ne(OrganizationAppStatus.DELETE.getCode()));

        OrganizationApp organizationApp = query.fetchAnyInto(OrganizationApp.class);

        return organizationApp;
    }

    private void prepareObj(OrganizationApp obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
