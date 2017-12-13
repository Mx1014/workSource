package com.everhomes.energy;

import java.util.List;

/**
 * Created by ying.xiong on 2017/12/11.
 */
public interface EnergyMeterFormulaMapProvider {
    void createEnergyMeterFormulaMap(EnergyMeterFormulaMap map);
    void updateEnergyMeterFormulaMap(EnergyMeterFormulaMap map);
    List<EnergyMeterFormulaMap> listEnergyMeterFormulaMap(Long communityId);
    List<Long> listCommunityIdByFormula(Long formulaId);
    EnergyMeterFormulaMap findEnergyMeterFormulaMap(Long community, Long formulaId);
}
