package com.everhomes.energy;

import java.util.List;

/**
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterFormulaProvider {

    /**
     * 根据id查找
     */
    EnergyMeterFormula findById(Integer namespaceId, Long id);

    /**
     * 根据名称查找
     */
    EnergyMeterFormula findByName(Integer namespaceId, Long communityId, String name);

    /**
     * 获取公式列表
     */
    List<EnergyMeterFormula> listMeterFormulas(Long ownerId, String ownerType, Long communityId, Integer namespaceId, Byte formulaType);

    /**
     * 删除公式
     */
    void deleteFormula(EnergyMeterFormula formula);

    /**
     * 新建公式
     */
    long createEnergyMeterFormula(EnergyMeterFormula formula);
}
