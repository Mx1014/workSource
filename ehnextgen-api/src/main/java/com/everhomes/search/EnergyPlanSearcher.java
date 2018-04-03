package com.everhomes.search;

import com.everhomes.energy.EnergyPlan;
import com.everhomes.rest.energy.SearchEnergyPlansCommand;
import com.everhomes.rest.energy.SearchEnergyPlansResponse;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/20.
 */
public interface EnergyPlanSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<EnergyPlan> plans);
    void feedDoc(EnergyPlan plan);
    void syncFromDb();
    SearchEnergyPlansResponse query(SearchEnergyPlansCommand cmd);

}
