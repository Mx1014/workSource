package com.everhomes.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBuildingsDao;
import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.server.schema.tables.records.EhBuildingAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhBuildingsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class BuildingProviderImpl implements BuildingProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingProviderImpl.class);
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
    private ShardingProvider shardingProvider;

	@Override
	public List<Building> ListBuildingsByCommunityId(ListingLocator locator, int count, Long communityId) {
		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBuildings.class, locator.getEntityId()));
		List<Building> buildings = new ArrayList<Building>();
        SelectQuery<EhBuildingsRecord> query = context.selectQuery(Tables.EH_BUILDINGS);
        query.addSelect(Tables.EH_BUILDINGS.fields());
        query.setDistinct(true);
    
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_BUILDINGS.ID.lt(locator.getAnchor()));
        }
        
        query.addConditions(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId));
        query.addOrderBy(Tables.EH_BUILDINGS.ID.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query buildings by count, sql=" + query.getSQL());
            LOGGER.debug("Query buildings by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((r) -> {
            buildings.add(ConvertHelper.convert(r, Building.class));
            return null;
        });
        
        if(buildings.size() > 0) {
            locator.setAnchor(buildings.get(buildings.size() -1).getId());
        }
        
        
		return buildings;
	}

	@Override
	public Building findBuildingById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBuildings.class, id));
        EhBuildingsDao dao = new EhBuildingsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Building.class);
	}

	@Override
	public void populateBuildingAttachments(final Building building) {
        if(building == null) {
            return;
        } else {
            List<Building> buildings = new ArrayList<Building>();
            buildings.add(building);
            
            populateBuildingAttachments(buildings);
        }
    }
    
	@Override
    public void populateBuildingAttachments(final List<Building> buildings) {
        if(buildings == null || buildings.size() == 0) {
            return;
        }
            
        final List<Long> buildingIds = new ArrayList<Long>();
        final Map<Long, Building> mapBuildings = new HashMap<Long, Building>();
        
        for(Building building: buildings) {
        	buildingIds.add(building.getId());
        	mapBuildings.put(building.getId(), building);
        }
        
        List<Integer> shards = this.shardingProvider.getContentShards(EhForumPosts.class, buildingIds);
        this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhBuildings.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhBuildingAttachmentsRecord> query = context.selectQuery(Tables.EH_BUILDING_ATTACHMENTS);
            query.addConditions(Tables.EH_BUILDING_ATTACHMENTS.BUILDING_ID.in(buildingIds));
            query.fetch().map((EhBuildingAttachmentsRecord record) -> {
                Building building = mapBuildings.get(record.getBuildingId());
                assert(building != null);
                building.getAttachments().add(ConvertHelper.convert(record, BuildingAttachment.class));
            
                return null;
            });
            return true;
        });
    }

}
