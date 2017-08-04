// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.statistics.event.StatEventPortalConfigType;
import com.everhomes.rest.statistics.event.StatEventPortalStatType;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;
import com.everhomes.server.schema.tables.EhStatEventPortalConfigs;
import com.everhomes.statistics.event.*;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * 底部导航栏
 * Created by xq.tian on 2017/8/7.
 */
@Component(AbstractStatEventHandler.EVENT_HANDLER_PREFIX + AbstractStatEventHandler.PORTAL_ON_BOTTOM_NAVIGATION_CLICK)
public class PortalOnBottomNavigationClickEventHandler extends AbstractStatEventHandler {

    @Autowired
    private StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    private StatEventPortalConfigProvider statEventPortalConfigProvider;

    @Autowired
    private StatEventParamProvider statEventParamProvider;

    @Autowired
    private StatEventParamLogProvider statEventParamLogProvider;

    @Override
    public String getEventName() {
        return PORTAL_ON_BOTTOM_NAVIGATION_CLICK;
    }

    @Override
    public List<StatEventStatistic> process(Namespace namespace, StatEvent statEvent, LocalDate statDate) {
        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));
        Date date = Date.valueOf(statDate);

        List<StatEventParam> params = statEventParamProvider.listParam(statEvent.getEventName(), statEvent.getEventVersion());

        StatEventParam param = new StatEventParam();
        for (StatEventParam p : params) {
            if (p.getParamKey().equals("identifier")) {
                param = p;
                break;
            }
        }

        Map<String, Integer> paramValueToCountMap = statEventParamLogProvider.countParamTotalCount(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), param.getParamKey(), minTime, maxTime);

        // 独立session数map: {参数值:次数}
        Map<String, Integer> countDistinctSessionMap = statEventParamLogProvider.countDistinctSession(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), param.getParamKey(), minTime, maxTime);

        // 独立用户数 map: {参数值:次数}
        Map<String, Integer> countDistinctUidMap = statEventParamLogProvider.countDistinctUid(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), param.getParamKey(), minTime, maxTime);

        List<StatEventStatistic> statList = new ArrayList<>();
        // map: {参数值:次数}
        for (Map.Entry<String, Integer> entry : paramValueToCountMap.entrySet()) {

            // 拿到底部导航栏的配置config
            StatEventPortalConfig portalConfig = statEventPortalConfigProvider.findPortalConfig(
                    namespace.getId(), StatEventPortalConfigType.BOTTOM_NAVIGATION.getCode(), entry.getKey());

            // 根据配置名称拿到底部导航栏的今天的统计数据
            StatEventPortalStatistic portalStat = statEventPortalStatisticProvider.findStatEventPortalStatistic(
                    namespace.getId(), StatEventPortalStatType.BOTTOM_NAVIGATION.getCode(), portalConfig.getConfigName(), date);

            StatEventStatistic eventStat = new StatEventStatistic();

            eventStat.setStatDate(date);
            eventStat.setNamespaceId(namespace.getId());

            eventStat.setTotalCount(entry.getValue().longValue());

            Integer completedSessions = countDistinctSessionMap.get(entry.getKey());
            eventStat.setCompletedSessions(completedSessions.longValue());

            Integer uniqueUsers = countDistinctUidMap.get(entry.getKey());
            eventStat.setUniqueUsers(uniqueUsers.longValue());

            eventStat.setEventName(statEvent.getEventName());
            eventStat.setEventDisplayName(statEvent.getEventDisplayName());
            eventStat.setEventPortalStatId(portalStat.getId());

            eventStat.setEventType(statEvent.getEventType());
            eventStat.setEventVersion(statEvent.getEventVersion());
            eventStat.setTimeInterval(StatEventStatTimeInterval.DAILY.getCode());

            eventStat.setOwnerType(EhStatEventPortalConfigs.class.getSimpleName());
            eventStat.setOwnerId(portalConfig.getId());

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("bottomNavigationName", portalStat.getDisplayName());
            paramMap.put("bottomNavigationDesc", portalStat.getDescription());

            eventStat.setParam(StringHelper.toJsonString(paramMap));

            statList.add(eventStat);
        }
        return statList;
    }
}
