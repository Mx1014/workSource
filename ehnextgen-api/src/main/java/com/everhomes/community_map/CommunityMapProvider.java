package com.everhomes.community_map;

import java.util.List;

/**
 * @author sw on 2017/8/14.
 */
public interface CommunityMapProvider {
    List<CommunityMapSearchType> listCommunityMapSearchTypesByNamespaceId(Integer namespaceId);

    CommunityMapInfo findCommunityMapInfo(Integer namespaceId);

    List<CommunityBuildingGeo> listCommunityBuildingGeos(Long BuildingId);

    void createCommunityMapShop(CommunityMapShopDetail communityMapShopDetail);

    void updateCommunityMapShop(CommunityMapShopDetail communityMapShopDetail);

    CommunityMapShopDetail getCommunityMapShopDetailById(Long id);

    List<CommunityMapShopDetail> searchCommunityMapShops(Integer namespaceId, String ownerType, Long ownerId,
                                                         Long buildingId, String keyword, Long pageAnchor, Integer pageSize);
}
