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
import com.everhomes.server.schema.tables.daos.EhServiceConfigurationsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceConfigurations;
import com.everhomes.server.schema.tables.records.EhServiceConfigurationsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.techpark.servicehotline.ServiceConfiguration;
import com.everhomes.techpark.servicehotline.ServiceConfigurationsProvider;
import com.everhomes.util.ConvertHelper;
@Component
public class ServiceConfigurationsProviderImpl implements ServiceConfigurationsProvider {
	 @Autowired
	    private DbProvider dbProvider;

	    @Autowired
	    private ShardingProvider shardingProvider;

	    @Autowired
	    private SequenceProvider sequenceProvider;

	    @Override
	    public Long createServiceConfiguration(ServiceConfiguration obj) {
	        String key = NameMapper.getSequenceDomainFromTablePojo(EhServiceConfigurations.class);
					long id = sequenceProvider.getNextSequence(key);
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        obj.setId(id);
	        prepareObj(obj);
	        EhServiceConfigurationsDao dao = new EhServiceConfigurationsDao(context.configuration());
	        dao.insert(obj);
	        return id;
	    }

	    @Override
	    public void updateServiceConfiguration(ServiceConfiguration obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhServiceConfigurationsDao dao = new EhServiceConfigurationsDao(context.configuration());
	        dao.update(obj);
	    }

	    @Override
	    public void deleteServiceConfiguration(ServiceConfiguration obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhServiceConfigurationsDao dao = new EhServiceConfigurationsDao(context.configuration());
	        dao.deleteById(obj.getId());
	    }

	    @Override
	    public void deleteServiceConfiguration(Long id) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhServiceConfigurationsDao dao = new EhServiceConfigurationsDao(context.configuration());
	        dao.deleteById(id);
	    }
	    @Override
	    public ServiceConfiguration getServiceConfigurationById(Long id) {
	        try {
	        ServiceConfiguration[] result = new ServiceConfiguration[1];
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        result[0] = context.select().from(Tables.EH_SERVICE_CONFIGURATIONS)
	            .where(Tables.EH_SERVICE_CONFIGURATIONS.ID.eq(id))
	            .fetchAny().map((r) -> {
	                return ConvertHelper.convert(r, ServiceConfiguration.class);
	            });

	        return result[0];
	        } catch (Exception ex) {
	            //fetchAny() maybe return null
	            return null;
	        }
	    }

	    @Override
	    public List<ServiceConfiguration> queryServiceConfigurations(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        SelectQuery<EhServiceConfigurationsRecord> query = context.selectQuery(Tables.EH_SERVICE_CONFIGURATIONS);
	        if(queryBuilderCallback != null)
	            queryBuilderCallback.buildCondition(locator, query);

	        if(null != locator && locator.getAnchor() != null) {
	            query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.ID.gt(locator.getAnchor()));
	            }

	        query.addLimit(count);
	        List<ServiceConfiguration> objs = query.fetch().map((r) -> {
	            return ConvertHelper.convert(r, ServiceConfiguration.class);
	        });

	        return objs;
	    }

	    private void prepareObj(ServiceConfiguration obj) {
	    }
}
