package com.everhomes.energy;

import java.util.List;

/**
 * Created by ying.xiong on 2017/3/16.
 */
public interface EnergyMeterPriceConfigProvider {

    Long createEnergyMeterPriceConfig(EnergyMeterPriceConfig config);
    EnergyMeterPriceConfig findById(Long id, Long ownerId, String ownerType, Long communityId, Integer namespaceId);
    EnergyMeterPriceConfig findById(Long id);
    EnergyMeterPriceConfig findByName(String name, Long ownerId, String ownerType, Long communityId, Integer namespaceId);
    List<EnergyMeterPriceConfig> listPriceConfig(Long ownerId, String ownerType, Long communityId, Integer namespaceId);

    void deletePriceConfig(EnergyMeterPriceConfig config);
}
