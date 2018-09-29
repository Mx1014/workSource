package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>currentCycleLanesAverage: 当前周期泳道平均处理时长</li>
 * <li>dtos: 参考{@com.everhomes.rest.flow_statistics.StatisticsByLanesDTO}</li>
 * </ul>
 */
public class StatisticsByLanesResponse {

    private List<StatisticsByLanesDTO> dtos ;
    private Double currentCycleLanesAverage ;//当前周期泳道平均处理时长

    public List<StatisticsByLanesDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<StatisticsByLanesDTO> dtos) {
        this.dtos = dtos;
    }

    public Double getCurrentCycleLanesAverage() {
        return currentCycleLanesAverage;
    }

    public void setCurrentCycleLanesAverage(Double currentCycleLanesAverage) {
        this.currentCycleLanesAverage = currentCycleLanesAverage;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
