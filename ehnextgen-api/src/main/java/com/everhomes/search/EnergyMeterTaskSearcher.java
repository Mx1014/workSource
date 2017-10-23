package com.everhomes.search;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/20.
 */
public interface EnergyMeterTaskSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<EnergyPlan> tasks);
    void feedDoc(EnergyPlan task);
    void syncFromDb();
    SearchEnergyPlansResponse query(SearchEnergyPlansCommand cmd);
}
