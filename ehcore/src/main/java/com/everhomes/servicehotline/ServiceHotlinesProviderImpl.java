package com.everhomes.servicehotline;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceHotlinesDao;
import com.everhomes.server.schema.tables.pojos.EhServiceHotlines;
import com.everhomes.server.schema.tables.records.EhServiceHotlinesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.techpark.servicehotline.ServiceHotline;
import com.everhomes.techpark.servicehotline.ServiceHotlinesProvider;
import com.everhomes.util.ConvertHelper;
@Component
public class ServiceHotlinesProviderImpl implements ServiceHotlinesProvider {
	@Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createServiceHotline(ServiceHotline obj) {
        String key = NameMapper.getSequenceDomainFromTablePojo(EhServiceHotlines.class);
				long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        obj.setId(id);
        prepareObj(obj);
        EhServiceHotlinesDao dao = new EhServiceHotlinesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateServiceHotline(ServiceHotline obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceHotlinesDao dao = new EhServiceHotlinesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteServiceHotline(ServiceHotline obj) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceHotlinesDao dao = new EhServiceHotlinesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ServiceHotline getServiceHotlineById(Long id) {
        try {
        ServiceHotline[] result = new ServiceHotline[1];
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        result[0] = context.select().from(Tables.EH_SERVICE_HOTLINES)
            .where(Tables.EH_SERVICE_HOTLINES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ServiceHotline.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ServiceHotline> queryServiceHotlines(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhServiceHotlinesRecord> query = context.selectQuery(Tables.EH_SERVICE_HOTLINES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(null != locator && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SERVICE_HOTLINES.ID.gt(locator.getAnchor()));
            }
        query.addOrderBy(Tables.EH_SERVICE_HOTLINES.DEFAULT_ORDER.asc());
        query.addLimit(count);
        List<ServiceHotline> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ServiceHotline.class);
        });

        return objs;
    }

    private void prepareObj(ServiceHotline obj) {
    }
}
