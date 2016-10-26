package com.everhomes.energy;

/**
 * 表记provider
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterProvider {
    /**
     * 创建表记
     * @return  返回id
     */
    long createMeter(Meter meter);

    /**
     * 根据id查询
     */
    Meter findById(Integer namespaceId, Long meterId);
}
