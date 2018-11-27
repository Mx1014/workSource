package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>laneId: 泳道 Id</li>
 * <li>laneName: 泳道名</li>
 * <li>lastCycleAverage: 上一周期平均处理耗时</li>
 * <li>currentCycleAverage: 当前周期平均处理耗时</li>
 * <li>earlyComparedVal: 环比效率值</li>
 * <li>laneLevel: 泳道的level</li>
 * </ul>
 */
public class StatisticsByLanesDTO {

    private Long laneId ;//(泳道 Id)
    private String laneName ; //(泳道名)
    private Double lastCycleAverage; //(上一周期平均处理耗时)
    private Double currentCycleAverage; //(当前周期平均处理耗时)
    private Double earlyComparedVal; //(环比效率值)
    private Integer laneLevel; //(泳道的level)

    public Long getLaneId() {
        return laneId;
    }

    public void setLaneId(Long laneId) {
        this.laneId = laneId;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public Double getLastCycleAverage() {
        return lastCycleAverage;
    }

    public void setLastCycleAverage(Double lastCycleAverage) {
        this.lastCycleAverage = lastCycleAverage;
    }

    public Double getCurrentCycleAverage() {
        return currentCycleAverage;
    }

    public void setCurrentCycleAverage(Double currentCycleAverage) {
        this.currentCycleAverage = currentCycleAverage;
    }

    public Double getEarlyComparedVal() {
        return earlyComparedVal;
    }

    public void setEarlyComparedVal(Double earlyComparedVal) {
        this.earlyComparedVal = earlyComparedVal;
    }

    public Integer getLaneLevel() {
        return laneLevel;
    }

    public void setLaneLevel(Integer laneLevel) {
        this.laneLevel = laneLevel;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
