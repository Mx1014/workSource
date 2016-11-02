package com.everhomes.energy;

import java.sql.Timestamp;

import com.everhomes.rest.energy.EnergyMeterSettingType;

/**
 * Created by xq.tian on 2016/10/31.
 */
public interface EnergyMeterSettingLogProvider {
    /**
     * 创建settingLog
     */
    long createSettingLog(EnergyMeterSettingLog log);

    /**
     * 获取表记的当前setting记录
     * @param settingType   setting的类型
     */
    EnergyMeterSettingLog findCurrentSettingByMeterId(Integer namespaceId, Long meterId, EnergyMeterSettingType settingType);

	EnergyMeterSettingLog findCurrentSettingByMeterId(Integer namespaceId, Long meterId, EnergyMeterSettingType settingType,
			Timestamp statDate);
}
