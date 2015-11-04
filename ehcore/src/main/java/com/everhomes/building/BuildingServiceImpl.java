package com.everhomes.building;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.forum.ForumServiceErrorCode;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostDTO;
import com.everhomes.forum.PostStatus;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class BuildingServiceImpl implements BuildingService {
	 private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);
	 
	 @Autowired
	 private BuildingProvider buildingProvider;
	 
	 @Autowired
	 private ConfigurationProvider configProvider;

	@Override
	public ListBuildingCommandResponse listBuildings(ListBuildingCommand cmd) {
		
		Long communityId = cmd.getCommunityId();
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
		List<Building> buildings = buildingProvider.ListBuildingsByCommunityId(locator, pageSize + 1,communityId);
		return null;
	}

	@Override
	public BuildingDTO getBuilding(GetBuildingCommand cmd) {
		
		Building building = buildingProvider.findBuildingById(cmd.getBuildingId());
		if(building != null) {
            if(BuildingStatus.ACTIVE != BuildingStatus.fromCode(building.getStatus())) {
            	
        		LOGGER.error("Building already deleted");
        		throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE, 
        				BuildingServiceErrorCode.ERROR_BUILDING_DELETED, "Building already deleted");
            }
			this.buildingProvider.populateBuildingAttachments(building);
	        populateBuilding(building, true);
	        return ConvertHelper.convert(building, BuildingDTO.class);
		}else {
            LOGGER.error("Building not found");
            throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE, 
            		BuildingServiceErrorCode.ERROR_BUILDING_NOT_FOUND, "Building not found");
        }
	}

	 private void populateBuilding(Building building, boolean isDetail) {
		 
	 }
}
