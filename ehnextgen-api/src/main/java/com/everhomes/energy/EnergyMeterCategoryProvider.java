package com.everhomes.energy;

import java.util.List;

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
    EnergyMeterCategory findByName(Integer namespaceId, Long communityId, String name);

    /**
     * 获取表记分类列表
     */
    List<EnergyMeterCategory> listMeterCategories(Integer namespaceId, Byte categoryType, Long ownerId, String ownerType, Long communityId);
 
	EnergyMeterCategory findById(Long id); 
    /**
     * 新建分类
     */
    long createEnergyMeterCategory(EnergyMeterCategory category);

    /**
     * 更新分类
     */
    void updateEnergyMeterCategory(EnergyMeterCategory category);

    /**
     * 删除分类
     */
    void deleteEnergyMeterCategory(EnergyMeterCategory category);
}
