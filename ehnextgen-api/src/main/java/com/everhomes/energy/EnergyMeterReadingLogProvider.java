package com.everhomes.energy;

/**
 * Created by xq.tian on 2016/11/1.
 */
public interface EnergyMeterReadingLogProvider {
    /**
     * 读表
     */
    long createEnergyMeterReadingLog(EnergyMeterReadingLog log);
}
