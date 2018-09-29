package com.everhomes.rest.flow_statistics;

import java.util.List;

/**
 * <ul>
 * <li>currentCycleNodesAverage: 当前周期节点平均处理时长</li>
 * <li>dtos: 参考{@com.everhomes.rest.flow_statistics.StatisticsByNodesDTO}</li>
 * </ul>
 */
public class StatisticsByNodesResponse {

    private List<StatisticsByNodesDTO> dtos ;
    private Double currentCycleNodesAverage;//当前周期节点平均处理时长

    public List<StatisticsByNodesDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<StatisticsByNodesDTO> dtos) {
        this.dtos = dtos;
    }

    public Double getCurrentCycleNodesAverage() {
        return currentCycleNodesAverage;
    }

    public void setCurrentCycleNodesAverage(Double currentCycleNodesAverage) {
        this.currentCycleNodesAverage = currentCycleNodesAverage;
    }
}
