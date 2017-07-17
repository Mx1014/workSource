// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventParamStatisticProvider {

	void createStatEventParamStatistic(StatEventParamStatistic statEventParamStatistic);

	void updateStatEventParamStatistic(StatEventParamStatistic statEventParamStatistic);

	StatEventParamStatistic findStatEventParamStatisticById(Long id);

    /**
     * 根据事件统计id查询参数统计数据
     */
    List<StatEventParamStatistic> listEventParamStat(List<Long> eventStatId);

}