package com.everhomes.energy;

/**
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterFormulaProvider {

    /**
     * 根据id查找
     */
    EnergyMeterFormula findById(Integer namespaceId, Long id);
}
