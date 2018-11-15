// @formatter:off
package com.everhomes.community;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.CommunityGeoPointDTO;

public interface CommunityProvider {
    void createCommunity(Long creatorId, Community community);
    void updateCommunity(Community community);
    void deleteCommunity(Community community);
    void deleteCommunityById(long id);
    Community findCommunityById(Long id);
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
	List<Community> listCommunitiesByKeyWord(ListingLocator locator, int i, String keyword, Integer namespaceId, Byte communityType);
	List<Community> findCommunitiesByNameCityIdAreaId(String name, Long cityId,Long areaId);
	
	List<Building> ListBuildingsByCommunityId(ListingLocator locator, int count, Long communityId, Integer namespaceId, String keyword);
	List<Building> ListBuildingsByCommunityId(ListingLocator locator, int count, Long communityId, Integer namespaceId, String keyword, Timestamp lastUpdateTime);

    List<Building> ListBuildingsBykeywordAndNameSpace(Integer namespaceId, String keyword);

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
    
    List<Community> listCommunitiesByCategory(Integer namespaceId, Long cityId, Long areaId, Long categoryId, String keyword, Long pageAnchor,
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
    Map<String, Long> listCommunityIdByNamespaceType(Integer namespaceId, String namespaceType);
    CommunityGeoPoint findCommunityGeoPointByCommunityId(long communityId);
    Community findCommunityByNamespaceToken(String namespaceType, String namespaceToken);
    List<Long> listCommunityByNamespaceToken(String namespaceType, List<String> namespaceToken);

    List<Community> listCommunities(Integer namespaceId, ListingLocator locator, Integer pageSize,
                                    ListingQueryBuilderCallback queryBuilderCallback);
									
									    //默认园区/小区
    Community findFirstCommunityByNameSpaceIdAndType(Integer namespaceId, Byte type);

    //获取对应的目标communityId
    Long findDefaultCommunityByCommunityId(Integer namespaceId, Long originId);
	List<Community> listCommunitiesByOrgId(ListingLocator locator, int i, Long orgId, String keyword);

    List<Community> listCommunities(Integer namespaceId, Byte communityType, Long orgId, String keyword, Byte status, Byte ownerFlag, ListingLocator locator, int count);

    //在给予的communityIds的范围内根据参数中的经纬度进行排序
    List<CommunityGeoPoint> listCommunityGeoPointByGeoHashInCommunities(double latitude, double longitude, int geoHashLength, List<Long> communityIds);

    List<Community> listCommunitiesByCityIdAndAreaId(Integer namespaceId, Long cityId, Long areaId, String keyword,List<Long> communityIds, Long pageAnchor,
                                                     Integer pageSize);


    String getCommunityToken(String tokenType, Long communityId);

    Community findCommunityByCommunityNumber(String communityNumber, Integer namespaceId);
    Building findBuildingByCommunityIdAndNumber(Long communityId, String buildingNumber);

    List<Community> listNamespaceCommunities(Integer namespaceId);
    List<Community> listAllCommunitiesWithNamespaceToken();

    Community findDefaultCommunity(Integer namespaceId);

    Community findAnyCommunity(Integer namespaceId);

    /**
     * 查询该域空间下不在该项目中的所有企业
     * @param communityId
     * @param namespaceId
     * @return
     */
    List<Long> findOrganizationIdsByNamespaceId(Long communityId , Integer namespaceId);

    /**
     * 根据项目名称和域空间Id来查询项目
     * @param name
     * @param namespaceId
     * @return
     */
    Community findCommunityByNameAndNamespaceId(String name,Integer namespaceId);
	
	   //导入项目信息，查询项目是否存在
	Community findCommunityByNamespaceIdAndName(Integer namespaceId, String name);
	Community findCommunityByNumber(String communityNumber, Integer namespaceId);
	Integer countActiveBuildingsByCommunityId(Long communityId);
	Integer countActiveApartmentsByCommunityId(Long communityId);
	List<Building> listBuildingsByKeywords(Integer namespaceId, Long communityId, Long buildingId, String keyWords,
			CrossShardListingLocator locator, int pageSize);
	Integer countRelatedContractNumberInCommunity(Long communityId);
	List<Community> findCommunitiesByNamespaceId(Integer nameSpaceId);
	List<Building> findBuildingsByCommunityId(Long communityId);
	List<Building> findBuildingsByNamespaceId(Integer namespaceId);
	Map<Long, Building> mapBuildingIdAndBuilding(List<Long> buildingIds);
	Long getOrganizationIdByCommunityId(Long communityId);
	String findCommunityCategoryByCommunityId(Long communityId);
	List<Building> findBuildingsByIds(List<Long> buildingIds);
	List<Long> findCommunityIdsByOrgId(Long organizationId);


    /**
     * Author: 黄鹏宇
     * Remark: 获取所有生效的业务用community
     */
    List<Long> listAllBizCommunities();
	
}
