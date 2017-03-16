package com.everhomes.energy;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/3/16.
 */
@Component
public class EnergyMeterPriceConfigProviderImpl implements EnergyMeterPriceConfigProvider {
    @Override
    public EnergyMeterPriceConfig findById(Long id, Long ownerId, String ownerType, Long communityId, Integer namespaceId) {
        return null;
    }
}
