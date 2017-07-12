package com.everhomes.energy;

import java.util.List;

/**
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyMeterFormulaVariableProvider {

    /**
     * 根据id查找
     */
    EnergyMeterFormulaVariable findById(Long id);

    /**
     * 根据逻辑名称查找
     */
    EnergyMeterFormulaVariable findByName(String name);

    /**
     * 根据显示名称查找
     */
    EnergyMeterFormulaVariable findByDisplayName(String displayName);

    /**
     * 获取公式变量列表
     */
    List<EnergyMeterFormulaVariable> listMeterFormulaVariables();
}
