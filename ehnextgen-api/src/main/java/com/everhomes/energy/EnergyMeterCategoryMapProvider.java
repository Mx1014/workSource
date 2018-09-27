package com.everhomes.energy;

import java.util.List;

/**
 * Created by ying.xiong on 2017/12/8.
 */
public interface EnergyMeterCategoryMapProvider {
    void createEnergyMeterCategoryMap(EnergyMeterCategoryMap map);
    void updateEnergyMeterCategoryMap(EnergyMeterCategoryMap map);
    List<EnergyMeterCategoryMap> listEnergyMeterCategoryMap(Long communityId,Long ownerId);
    List<Long> listCommunityIdByCategory(Long categoryId);
    EnergyMeterCategoryMap findEnergyMeterCategoryMap(Long community, Long categoryId);
}
