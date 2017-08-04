// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.namespace.Namespace;
import com.everhomes.news.News;
import com.everhomes.news.NewsProvider;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.statistics.event.StatEventPortalStatType;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;
import com.everhomes.server.schema.tables.EhNews;
import com.everhomes.server.schema.tables.EhPortalItemGroups;
import com.everhomes.server.schema.tables.EhPortalLayouts;
import com.everhomes.statistics.event.*;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 新闻点击
 * Created by xq.tian on 2017/8/7.
 */
@Component(AbstractStatEventHandler.EVENT_HANDLER_PREFIX + AbstractStatEventHandler.LAUNCHPAD_ON_NEWS_ITEM_CLICK)
public class LaunchpadOnNewsItemClickEventHandler extends AbstractStatEventHandler {

    @Autowired
    private StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    private StatEventParamProvider statEventParamProvider;

    @Autowired
    private StatEventParamLogProvider statEventParamLogProvider;

    @Autowired
    @Qualifier("PortalLaunchPadMappingProvider-LaunchPad")
    private PortalLaunchPadMappingProvider portalLaunchPadMappingProvider;

    @Autowired
    @Qualifier("PortalItemGroupProvider-LaunchPad")
    private PortalItemGroupProvider portalItemGroupProvider;

    @Autowired
    private NewsProvider newsProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_NEWS_ITEM_CLICK;
    }

    @Override
    public List<StatEventStatistic> process(Namespace namespace, StatEvent statEvent, LocalDate statDate) {
        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));
        Date date = Date.valueOf(statDate);

        List<StatEventParam> params = statEventParamProvider.listParam(statEvent.getEventName(), statEvent.getEventVersion());

        StatEventParam newsIdParam = new StatEventParam();
        StatEventParam layoutIdParam = new StatEventParam();
        for (StatEventParam p : params) {
            if (p.getParamKey().equals("id")) {
                newsIdParam = p;
            } else if (p.getParamKey().equals("layoutId")) {
                layoutIdParam = p;
            }
        }

        List<StatEventParamLog> eventParamLogs = statEventParamLogProvider.listEventParamLog(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), minTime, maxTime);

        Map<Long, List<StatEventParamLog>> eventLogIdToParamLogListMap = eventParamLogs.stream().collect(Collectors.groupingBy(StatEventParamLog::getEventLogId));

        // 从paramLog组装activityItemId与layoutId映射的map
        Map<Long, Long> newsItemIdToLayoutIdMap = new HashMap<>();
        for (Map.Entry<Long, List<StatEventParamLog>> entry : eventLogIdToParamLogListMap.entrySet()) {
            List<StatEventParamLog> value = entry.getValue();
            Long newsId = null;
            Long layoutId = null;
            for (StatEventParamLog log : value) {
                if (log.getParamKey().equals(newsIdParam.getParamKey())) {
                    newsId = Long.valueOf(log.getStringValue());
                } else if (log.getParamKey().equals(layoutIdParam.getParamKey())) {
                    layoutId = Long.valueOf(log.getStringValue());
                }
                if (newsId != null && layoutId != null) {
                    break;
                }
            }
            newsItemIdToLayoutIdMap.putIfAbsent(newsId, layoutId);
        }

        // map: {参数值:次数}
        Map<String, Integer> newsIdParamValueToCountMap = statEventParamLogProvider.countParamTotalCount(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), newsIdParam.getParamKey(), minTime, maxTime);

        // 独立session数map: {参数值:次数}
        Map<String, Integer> countDistinctSessionMap = statEventParamLogProvider.countDistinctSession(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), newsIdParam.getParamKey(), minTime, maxTime);

        // 独立用户数 map: {参数值:次数}
        Map<String, Integer> countDistinctUidMap = statEventParamLogProvider.countDistinctUid(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), newsIdParam.getParamKey(), minTime, maxTime);

        List<StatEventStatistic> statList = new ArrayList<>();
        // map: {参数值:次数}
        for (Map.Entry<String, Integer> entry : newsIdParamValueToCountMap.entrySet()) {

            Long newsId = Long.valueOf(entry.getKey());
            Long layoutId = newsItemIdToLayoutIdMap.get(newsId);

            News news = newsProvider.findNewsById(newsId);
            if (news == null) {
                continue;
            }

            // 去映射表里拿到门户配置的layoutId => 去portalItemGroup里拿到portalItemGroup => 去portalStatistic里拿到统计记录的id => 保存数据
            PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalLayouts.class.getSimpleName(), layoutId);

            PortalItemGroup portalItemGroup = portalItemGroupProvider.findPortalItemGroup(mapping.getPortalContentId(), Widget.NEWS.getCode(), "Default");
            if (portalItemGroup == null) {
                continue;
            }

            StatEventPortalStatistic portalStat = statEventPortalStatisticProvider.findStatEventPortalStatistic(
                    namespace.getId(), StatEventPortalStatType.PORTAL_ITEM_GROUP.getCode(), EhPortalItemGroups.class.getSimpleName(),
                    portalItemGroup.getId(), date);
            if (portalStat == null) {
                continue;
            }

            StatEventStatistic eventStat = new StatEventStatistic();

            eventStat.setStatDate(date);
            eventStat.setNamespaceId(namespace.getId());

            eventStat.setTotalCount(entry.getValue().longValue());

            Integer completedSessions = countDistinctSessionMap.get(entry.getKey());
            eventStat.setCompletedSessions(completedSessions.longValue());

            Integer uniqueUsers = countDistinctUidMap.get(entry.getKey());
            eventStat.setUniqueUsers(uniqueUsers.longValue());

            eventStat.setEventName(statEvent.getEventName());
            eventStat.setEventPortalStatId(portalStat.getId());
            eventStat.setEventDisplayName(statEvent.getEventDisplayName());

            eventStat.setEventType(statEvent.getEventType());
            eventStat.setEventVersion(statEvent.getEventVersion());
            eventStat.setTimeInterval(StatEventStatTimeInterval.DAILY.getCode());

            eventStat.setOwnerType(EhNews.class.getSimpleName());
            eventStat.setOwnerId(newsId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("newsId", newsId);
            paramMap.put("newsTitle", news.getTitle());

            eventStat.setParam(StringHelper.toJsonString(paramMap));

            statList.add(eventStat);
        }
        return statList;
    }
}
