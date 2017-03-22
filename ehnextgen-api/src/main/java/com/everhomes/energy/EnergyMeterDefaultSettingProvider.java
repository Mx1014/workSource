package com.everhomes.energy;

import com.everhomes.rest.energy.EnergyMeterSettingType;

import java.util.List;

/**
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterDefaultSettingProvider {

    /**
     * 根据id获取默认设置
     */
    EnergyMeterDefaultSetting findById(Integer namespaceId, Long id);

    /**
     * 更新默认设置
     */
    void updateEnergyMeterDefaultSetting(EnergyMeterDefaultSetting setting);

    /**
     * 默认设置列表
     */
    List<EnergyMeterDefaultSetting> listDefaultSetting(Long ownerId, String ownerType, Long communityId, Integer namespaceId, Byte meterType);

    EnergyMeterDefaultSetting findBySettingType(Integer namespaceId, EnergyMeterSettingType settingType);

    /**
     * 新增默认设置
     */
    void createEnergyMeterDefaultSetting(EnergyMeterDefaultSetting setting);
}
