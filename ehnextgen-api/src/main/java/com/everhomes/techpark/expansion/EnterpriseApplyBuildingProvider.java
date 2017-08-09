package com.everhomes.techpark.expansion;

import java.util.List;

/**
 * @author sw on 2017/8/3.
 */
public interface EnterpriseApplyBuildingProvider {

    List<LeaseBuilding> listLeaseBuildings(Integer namespaceId, Long communityId, Long pageAnchor, Integer pageSize);

    void createLeaseBuilding(LeaseBuilding leaseBuilding);

    void updateLeaseBuilding(LeaseBuilding leaseBuilding);

    LeaseBuilding findLeaseBuildingById(Long id);

    LeaseBuilding findLeaseBuildingByBuildingId(Long buildingId);

    void createLeasePromotionCommunity(LeasePromotionCommunity leasePromotionCommunity);

    List<Long> listLeasePromotionCommunities(Long id);

    void deleteLeasePromotionCommunity(Long id);

    Boolean verifyBuildingName(Integer namespaceId, Long communityId, String buildingName);

    void createLeaseBuildings(List<LeaseBuilding> leaseBuildings);

}
