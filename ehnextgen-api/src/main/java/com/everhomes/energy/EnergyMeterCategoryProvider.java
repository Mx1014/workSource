package com.everhomes.energy;

/**
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterCategoryProvider {

    /**
     * 根据id获取category
     */
    EnergyMeterCategory findById(Integer namespaceId, Long id);

    /**
     * 根据显示名称获取category
     */
    EnergyMeterCategory findByName(Integer namespaceId, String name);
}
