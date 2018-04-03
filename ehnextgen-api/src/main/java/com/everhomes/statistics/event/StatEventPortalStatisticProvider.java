// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Date;
import java.util.List;

public interface StatEventPortalStatisticProvider {

	void createStatEventPortalStatistic(StatEventPortalStatistic statEventPortalStatistic);

	void updateStatEventPortalStatistic(StatEventPortalStatistic statEventPortalStatistic);

	StatEventPortalStatistic findStatEventPortalStatisticById(Long id);

    /**
     * 门户统计结果列表
     */
    List<StatEventPortalStatistic> listEventPortalStatByZeroParentId(Integer namespaceId, Byte statType, Date startDate, Date endDate);

    List<StatEventPortalStatistic> listEventPortalStatByParentId(Integer namespaceId, Long parentId, Byte statType, Date startDate, Date endDate);

    /**
     * 根据时间查询identifier为所需的id列表
     */
    List<Long> listEventPortalStatByIdentifier(Integer namespaceId, Long parentId, String identifier, Date startDate, Date endDate);

    StatEventPortalStatistic findStatEventPortalStatistic(Integer namespaceId, byte statType, String configName, Date statDate);

    StatEventPortalStatistic findStatEventPortalStatistic(Integer namespaceId, byte statType, String ownerType, Long ownerId, Date statDate);

    void deleteEventPortalStatByDate(Date date);
}