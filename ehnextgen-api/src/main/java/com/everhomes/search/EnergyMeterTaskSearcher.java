package com.everhomes.search;

import com.everhomes.energy.EnergyMeterTask;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanCommand;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanResponse;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/20.
 */
public interface EnergyMeterTaskSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<EnergyMeterTask> tasks);
    void feedDoc(EnergyMeterTask task);
    void syncFromDb();
    SearchTasksByEnergyPlanResponse searchTasksByEnergyPlan(SearchTasksByEnergyPlanCommand cmd);
}
