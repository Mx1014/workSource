package com.everhomes.energy;

import com.everhomes.rest.energy.EnergyMeterSettingType;

import java.sql.Timestamp;
import java.util.List;

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

    /**
     * 查询引用传入的公式的setting
     * @param formulaId 需要查询的公式id
     * @return 返回其中一条数据
     */
    EnergyMeterSettingLog findSettingByFormulaId(Integer namespaceId, Long formulaId);
    EnergyMeterSettingLog findSettingByPriceConfigId(Integer namespaceId, Long priceConfigId);

    List<EnergyMeterSettingLog> listEnergyMeterSettingLogs(Integer namespaceId, Long meterId, Byte settingType);
    List<EnergyMeterSettingLog> listEnergyMeterSettingLogsOrderByCreateTime(Integer namespaceId, Long meterId, Byte settingType);
}
