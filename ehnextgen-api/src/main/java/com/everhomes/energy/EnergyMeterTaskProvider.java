package com.everhomes.energy;

import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/23.
 */
public interface EnergyMeterTaskProvider {
    void createEnergyMeterTask(EnergyMeterTask task);
    void updateEnergyMeterTask(EnergyMeterTask plan);
    EnergyMeterTask findEnergyMeterTaskById(Long planId);
    List<EnergyMeterTask> listEnergyMeterTasks(long pageAnchor, int pageSize);

    Map<Long, EnergyMeterTask> listEnergyMeterTasks(List<Long> ids);
}
