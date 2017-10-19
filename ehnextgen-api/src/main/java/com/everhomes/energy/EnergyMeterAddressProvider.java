package com.everhomes.energy;

import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/19.
 */
public interface EnergyMeterAddressProvider {
    Map<Long, EnergyMeterAddress> findByMeterId(Long meterId);

    void createEnergyMeterAddress(EnergyMeterAddress address);
    void updateEnergyMeterAddress(EnergyMeterAddress address);
}
