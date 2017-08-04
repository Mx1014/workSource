// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Date;
import java.util.List;

public interface StatEventStatisticProvider {

	void createStatEventStatistic(StatEventStatistic statEventStatistic);

	void updateStatEventStatistic(StatEventStatistic statEventStatistic);

	StatEventStatistic findStatEventStatisticById(Long id);

    /**
     * 根据门户统计数据的id列表查询事件统计数据的id列表
     */
    List<Long> listEventStatIdByPortalStatIds(List<Long> portalStatIdList);

    void insertEventStatList(List<StatEventStatistic> statList);

    List<StatEventStatistic> countAndListEventStat(Integer namespaceId, Long parentId, String identifier, Date startDate, Date endDate);

    // List<StatEventStatistic> listStatEventStatistic();

}