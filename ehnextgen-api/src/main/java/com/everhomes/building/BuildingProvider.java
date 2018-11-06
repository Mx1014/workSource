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
	List<Building> listBuildingByNamespaceType(Integer namespaceId, String namespaceType);


	/**
	 * 根据项目编号communityId和域空间IdnamespaceId来查询eh_buildings表中的信息
	 * @param communityId
	 * @param namespaceId
	 * @param buildingName
	 * @return
	 */
	List<Building> getBuildingByCommunityIdAndNamespaceId(Long communityId,Integer namespaceId,String buildingName);

	/**
	 * 根据域空间Id和项目编号来查询楼栋
	 * @param namespaceId
	 * @param communityId
	 * @return
	 */
	List<Building> findBuildingByNamespaceIdAndCommunityId(Integer namespaceId,Long communityId);

}