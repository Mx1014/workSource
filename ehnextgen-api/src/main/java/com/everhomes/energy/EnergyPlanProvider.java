package com.everhomes.energy;

import com.everhomes.rest.equipment.ExecuteGroupAndPosition;

import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/19.
 */
public interface EnergyPlanProvider {
    void createEnergyPlan(EnergyPlan plan);
    void updateEnergyPlan(EnergyPlan plan);
    EnergyPlan findEnergyPlanById(Long planId);
    List<EnergyPlan> listEnergyPlans(long pageAnchor, int pageSize);
    List<EnergyPlan> listActivePlan();

    void createEnergyPlanGroupMap(EnergyPlanGroupMap groupMap);
    void deleteEnergyPlanGroupMap(EnergyPlanGroupMap groupMap);
    List<EnergyPlanGroupMap> listGroupsByEnergyPlan(Long planId);
    Map<Long, EnergyPlanGroupMap> listGroupMapsByEnergyPlan(Long planId);
    List<EnergyPlanGroupMap> lisEnergyPlanGroupMapByGroupAndPosition(List<ExecuteGroupAndPosition> groups);

    void createEnergyPlanMeterMap(EnergyPlanMeterMap meterMap);
    void updateEnergyPlanMeterMap(EnergyPlanMeterMap meterMap);
    void deleteEnergyPlanMeterMap(EnergyPlanMeterMap meterMap);
    List<EnergyPlanMeterMap> listMetersByEnergyPlan(Long planId);
    Map<Long, EnergyPlanMeterMap> listMeterMapsByEnergyPlan(Long planId);
}
