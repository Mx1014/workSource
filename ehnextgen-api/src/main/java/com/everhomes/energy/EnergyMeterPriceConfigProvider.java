package com.everhomes.energy;

/**
 * Created by Administrator on 2017/3/16.
 */
public interface EnergyMeterPriceConfigProvider {

    EnergyMeterPriceConfig findById(Long id, Long ownerId, String ownerType, Long communityId, Integer namespaceId);
}
