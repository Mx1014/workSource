// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.statistics.event.StatEventPortalStatType;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;
import com.everhomes.server.schema.tables.EhPortalItemGroups;
import com.everhomes.server.schema.tables.EhPortalLayouts;
import com.everhomes.statistics.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/8/7.
 */
abstract public class AbstractStatEventPortalItemGroupHandler extends AbstractStatEventHandler {

    @Autowired
    protected StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    protected StatEventParamProvider statEventParamProvider;

    @Autowired
    protected StatEventParamLogProvider statEventParamLogProvider;

    @Autowired
    @Qualifier("PortalLaunchPadMappingProvider-LaunchPad")
    protected PortalLaunchPadMappingProvider portalLaunchPadMappingProvider;

    @Autowired
    @Qualifier("PortalItemGroupProvider-LaunchPad")
    protected PortalItemGroupProvider portalItemGroupProvider;

    @Override
    public List<StatEventStatistic> process(Namespace namespace, StatEvent statEvent, LocalDate statDate, StatEventStatTimeInterval interval) {
        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));
        Date date = Date.valueOf(statDate);

        List<StatEventParam> params = statEventParamProvider.listParam(statEvent.getEventName(), statEvent.getEventVersion());

        List<StatEventParam> processedParams = getParams(params);
        if (processedParams == null) {
            throw new RuntimeException("processedParams not found");
        }

        // List<StatEventParamLog> eventParamLogs = statEventParamLogProvider.listEventParamLog(
        //         namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), minTime, maxTime);

        // Map<Long, List<StatEventParamLog>> eventLogIdToParamLogListMap = eventParamLogs.stream().collect(Collectors.groupingBy(StatEventParamLog::getEventLogId));

        // 从paramLog组装identifierParamValue与layoutId映射的map
        // Map<String, Long> identifierParamToValueToLayoutIdMap = new HashMap<>();
        // for (Map.Entry<Long, List<StatEventParamLog>> entry : eventLogIdToParamLogListMap.entrySet()) {
        //     List<StatEventParamLog> value = entry.getValue();
        //     String identifierParamValue = null;
        //     Long layoutId = null;
        //     for (StatEventParamLog log : value) {
        //         if (log.getParamKey().equals(identifierParam.getParamKey())) {
        //             identifierParamValue = log.getStringValue();
        //             // 如果identifierParam就是layoutId的时候
        //             if (identifierParam.getParamKey().equals("layoutId")) {
        //                 layoutId = Long.valueOf(log.getStringValue());
        //             }
        //         } else if (log.getParamKey().equals("layoutId")) {
        //             layoutId = Long.valueOf(log.getStringValue());
        //         }
        //         if (identifierParamValue != null && layoutId != null) {
        //             break;
        //         }
        //     }
        //     identifierParamToValueToLayoutIdMap.putIfAbsent(identifierParamValue, layoutId);
        // }

        // map: {参数值:次数}
        // Map<String, Integer> identifierParamValueToCountMap = statEventParamLogProvider.countParamTotalCount(
        //         namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), identifierParam.getParamKey(), minTime, maxTime);

        List<String> keys = processedParams.stream().map(StatEventParam::getParamKey).collect(Collectors.toList());

        Map<Map<String, String>, Integer> identifierParamValueToCountMap = statEventParamLogProvider.countParamTotalCount(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), keys, minTime, maxTime);

        // 独立session数map: {参数值:次数}
        // Map<String, Integer> countDistinctSessionMap = statEventParamLogProvider.countDistinctSession(
        //         namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), identifierParam.getParamKey(), minTime, maxTime);
        //
        // // 独立用户数 map: {参数值:次数}
        // Map<String, Integer> countDistinctUidMap = statEventParamLogProvider.countDistinctUid(
        //         namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), identifierParam.getParamKey(), minTime, maxTime);

        List<StatEventStatistic> statList = new ArrayList<>();
        // map: {参数值:次数}
        for (Map.Entry<Map<String, String>, Integer> entry : identifierParamValueToCountMap.entrySet()) {

            Map<String, String> map = entry.getKey();

            Long layoutId = Long.valueOf(map.get("layoutId"));

            // 去映射表里拿到门户配置的layoutId => 去portalItemGroup里拿到portalItemGroup => 去portalStatistic里拿到统计记录的id => 保存数据
            PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalLayouts.class.getSimpleName(), layoutId);

            PortalItemGroup portalItemGroup = portalItemGroupProvider.findPortalItemGroup(mapping.getPortalContentId(), getWidget().getCode(), getItemGroup(map));
            if (portalItemGroup == null) {
                continue;
            }

            StatEventPortalStatistic portalStat = statEventPortalStatisticProvider.findStatEventPortalStatistic(
                    namespace.getId(), StatEventPortalStatType.PORTAL_ITEM_GROUP.getCode(), EhPortalItemGroups.class.getSimpleName(),
                    portalItemGroup.getId(), date);
            if (portalStat == null) {
                continue;
            }

            StatEventStatistic eventStat = getEventStat(map);
            if (eventStat == null) {
                continue;
            }

            eventStat.setStatDate(date);
            eventStat.setNamespaceId(namespace.getId());
            eventStat.setTotalCount(entry.getValue().longValue());

            // Integer completedSessions = countDistinctSessionMap.get(map);
            // eventStat.setCompletedSessions(completedSessions.longValue());
            //
            // Integer uniqueUsers = countDistinctUidMap.get(map);
            // eventStat.setUniqueUsers(uniqueUsers.longValue());

            eventStat.setEventName(statEvent.getEventName());
            eventStat.setEventPortalStatId(portalStat.getId());
            eventStat.setEventDisplayName(statEvent.getEventDisplayName());

            eventStat.setEventType(statEvent.getEventType());
            eventStat.setEventVersion(statEvent.getEventVersion());
            eventStat.setTimeInterval(interval.getCode());

            eventStat.setEventName(statEvent.getEventName());

            statList.add(eventStat);
        }
        return statList;
    }

    protected abstract String getItemGroup(Map<String, String> paramsToValueMap);

    abstract protected Widget getWidget();

    abstract protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap);

    abstract protected List<StatEventParam> getParams(List<StatEventParam> params);
}
