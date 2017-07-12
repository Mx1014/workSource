package com.everhomes.techpark.expansion;

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
import com.everhomes.server.schema.tables.daos.EhEnterpriseOpRequestBuildingsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequestBuildings;
import com.everhomes.server.schema.tables.records.EhEnterpriseOpRequestBuildingsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class EnterpriseOpRequestBuildingProviderImpl implements EnterpriseOpRequestBuildingProvider {
	 @Autowired
	    private DbProvider dbProvider;

	    @Autowired
	    private ShardingProvider shardingProvider;

	    @Autowired
	    private SequenceProvider sequenceProvider;

	    @Override
	    public Long createEnterpriseOpRequestBuilding(EnterpriseOpRequestBuilding obj) {
	        String key = NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseOpRequestBuildings.class);
					long id = sequenceProvider.getNextSequence(key);
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        obj.setId(id);
	        prepareObj(obj);
	        EhEnterpriseOpRequestBuildingsDao dao = new EhEnterpriseOpRequestBuildingsDao(context.configuration());
	        dao.insert(obj);
	        return id;
	    }

	    @Override
	    public void updateEnterpriseOpRequestBuilding(EnterpriseOpRequestBuilding obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhEnterpriseOpRequestBuildingsDao dao = new EhEnterpriseOpRequestBuildingsDao(context.configuration());
	        dao.update(obj);
	    }

	    @Override
	    public void deleteEnterpriseOpRequestBuilding(EnterpriseOpRequestBuilding obj) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
	        EhEnterpriseOpRequestBuildingsDao dao = new EhEnterpriseOpRequestBuildingsDao(context.configuration());
	        dao.deleteById(obj.getId());
	    }

	    @Override
	    public EnterpriseOpRequestBuilding getEnterpriseOpRequestBuildingById(Long id) {
	        try {
	        EnterpriseOpRequestBuilding[] result = new EnterpriseOpRequestBuilding[1];
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        result[0] = context.select().from(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS)
	            .where(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.ID.eq(id))
	            .fetchAny().map((r) -> {
	                return ConvertHelper.convert(r, EnterpriseOpRequestBuilding.class);
	            });

	        return result[0];
	        } catch (Exception ex) {
	            //fetchAny() maybe return null
	            return null;
	        }
	    }

	    @Override
	    public List<EnterpriseOpRequestBuilding> queryEnterpriseOpRequestBuildings(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
	        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());

	        SelectQuery<EhEnterpriseOpRequestBuildingsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS);
	        if(queryBuilderCallback != null)
	            queryBuilderCallback.buildCondition(locator, query);

	        if(null != locator && locator.getAnchor() != null) {
	            query.addConditions(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.ID.gt(locator.getAnchor()));
	            }

	        query.addLimit(count);
	        List<EnterpriseOpRequestBuilding> objs = query.fetch().map((r) -> {
	            return ConvertHelper.convert(r, EnterpriseOpRequestBuilding.class);
	        });

	        return objs;
	    }

	    private void prepareObj(EnterpriseOpRequestBuilding obj) {
	    }
}
