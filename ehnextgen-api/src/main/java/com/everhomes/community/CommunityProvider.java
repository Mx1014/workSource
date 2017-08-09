// @formatter:off
package com.everhomes.community;

import java.util.List;
import java.util.Map;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.CommunityGeoPointDTO;

public interface CommunityProvider {
    void createCommunity(Long creatorId, Community community);
    void updateCommunity(Community community);
    void deleteCommunity(Community community);
    void deleteCommunityById(long id);
    Community findCommunityById(long id);
    List<CommunityGeoPoint> listCommunityGeoPoints(long id);
    
    void createCommunityGeoPoint(CommunityGeoPoint geoPoint);
    void updateCommunityGeoPoint(CommunityGeoPoint geoPoint);
    void deleteCommunityGeoPoint(CommunityGeoPoint geoPoint);
    CommunityGeoPoint findCommunityGeoPointById(long id);
    List<CommunityGeoPoint> findCommunityGeoPointByGeoHash(double latitude, double longitude, int geoHashLength);

    List<Community> listAllCommunities(long pageOffset, long pageSize);
    List<Community> findNearyByCommunityById(Integer namespaceId, long communityId);
    List<NearbyCommunityMap> findNearbyCommunityMap(Integer namespaceId, Long communityId);
    List<Community> findFixNearbyCommunityById(Integer namespaceId, Long communityId);
    List<Community> calculateNearbyCommunityByGeoPoints(Long communityId, Integer namespaceId);
    List<Community> listCommunitiesByStatus(ListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    
    Community findCommunityByAreaIdAndName(Long areaId,String name);
    
    List<Community> findCommunitiesByNameAndCityId(String name, long cityId, int namespaceId);
    List<Community> findCommunitiesByIds(List<Long> ids);
    Community findCommunityByUuid(String uuid);
	List<Community> listCommunitiesByKeyWord(ListingLocator locator, int i,
			String keyword);
	List<Community> findCommunitiesByNameCityIdAreaId(String name, Long cityId,Long areaId);
	
	List<Building> ListBuildingsByCommunityId(ListingLocator locator, int count, Long communityId, Integer namespaceId);
	
	Building findBuildingById(Long id);
	
	void populateBuildingAttachments(final Building building);
	
	void populateBuildingAttachments(final List<Building> buildings);
	
	Building createBuilding(Long creatorId, Building building);
	
	void updateBuilding(Building building);
	
	void deleteBuilding(Building building);
	
	void createBuildingAttachment(BuildingAttachment attachment);
	
	Boolean verifyBuildingName(Long communityId, String buildingName);
    
	List<Building> listBuildingsByStatus(ListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
	
	int countBuildingsBycommunityId(Long communityId);
	
	List<Long> listBuildingIdByCommunityId(Long communityId);
	
	List<CommunityUser> listUserCommunities(CommunityUser communityUser,int pageOffset,int pageSize);
	void deleteBuildingAttachmentsByBuildingId(Long buildingId);
	
	Building findBuildingByCommunityIdAndName(long communityId, String buildingName);
	
	List<Community> listCommunitiesByNamespaceId(Integer namespaceId);
	
	List<CommunityDTO> listCommunitiesByNamespaceId(Byte communityType, Integer namespaceId, ListingLocator locator, int pageSize);
	
	List<CommunityDTO> listCommunitiesByType(int namespaceId, List<Long> communityIds, Byte communityType, ListingLocator locator, int pageSize);

    List<Community> findCommunitiesByCityId(ListingLocator locator, int count, int namespaceId, long cityId);

    /*--------添加修改资源分类 ------*/
    void createResourceCategory(ResourceCategory resourceCategory);
    
    ResourceCategory findResourceCategoryById(Long id);
    
    void updateResourceCategory(ResourceCategory resourceCategory);
    
    void createResourceCategoryAssignment(ResourceCategoryAssignment resourceCategoryAssignment);
    
    ResourceCategoryAssignment findResourceCategoryAssignment(Long resourceId, String resourceType, Integer namespaceId);
    
    void deleteResourceCategoryAssignmentById(Long id);
    
    ResourceCategory findResourceCategoryByParentIdAndName(Long ownerId, String ownerType, Long parentId, String name, Byte type);
    
    void updateResourceCategoryAssignment(ResourceCategoryAssignment resourceCategoryAssignment);
    
    List<Community> listCommunitiesByCategory(Long cityId, Long areaId, Long categoryId, String keyword, Long pageAnchor, 
			Integer pageSize);
    
    List<ResourceCategory> listResourceCategory(Long ownerId, String ownerType, Long parentId, String path, Byte type);
    
    List<ResourceCategoryAssignment> listResourceCategoryAssignment(Long categoryId, Integer namespaceId);

    List<Community> listCommunitiesByFeedbackForumId(Long feedbackForumId);
    List<ResourceCategory> listResourceCategory(Long ownerId, String ownerType, List<Long> ids, Byte type);
    void deleteResourceCategoryById(Long id);

    List<ResourceCategoryAssignment> listResourceCategoryAssignment(Long categoryId, Integer namespaceId, String resourceType, List<Long> resourceIds);

    Map<Long, Community> listCommunitiesByIds(List<Long> ids);
    List<Community> listCommunityByNamespaceIdAndName(Integer namespaceId, String communityName);
    List<Community> listCommunityByNamespaceType(Integer namespaceId, String namespaceType);
}
