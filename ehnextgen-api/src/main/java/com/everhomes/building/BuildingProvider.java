// @formatter:off
package com.everhomes.building;

import java.util.List;

public interface BuildingProvider {

	void createBuilding(Building building);

	void updateBuilding(Building building);

	Building findBuildingById(Long id);

	List<Building> listBuilding();

	Building findBuildingByName(Integer namespaceId, Long communityId, String buildingName);

	void deleteBuilding(Building building);

	List<Building> listBuildingByNamespaceType(Integer namespaceId, Long communityId, String namespaceType);

}