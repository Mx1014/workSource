// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.statistics.event.*;
import com.everhomes.server.schema.tables.EhStatEventPortalConfigs;
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

/**
 * Created by xq.tian on 2017/8/7.
 */
abstract public class AbstractStatEventPortalConfigHandler extends AbstractStatEventHandler {

    @Autowired
    protected StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    protected StatEventParamProvider statEventParamProvider;

    @Autowired
    protected StatEventParamLogProvider statEventParamLogProvider;

    @Autowired
    private StatEventPortalConfigProvider statEventPortalConfigProvider;

    @Autowired
    @Qualifier("PortalLaunchPadMappingProvider-LaunchPad")
    protected PortalLaunchPadMappingProvider portalLaunchPadMappingProvider;

    @Autowired
    @Qualifier("PortalItemGroupProvider-LaunchPad")
    protected PortalItemGroupProvider portalItemGroupProvider;

    @Override
    public List<StatEventStatistic> processStat(Namespace namespace, StatEvent statEvent, LocalDate statDate, StatEventStatTimeInterval interval) {
        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));
        Date date = Date.valueOf(statDate);

        List<StatEventParam> params = statEventParamProvider.listParam(statEvent.getEventName(), statEvent.getEventVersion());

        StatEventParam identifierParam = getIdentifierParam(params);
        if (identifierParam == null) {
            throw new RuntimeException("identifier param not found");
        }

        // map: {参数值:次数}
        Map<String, StatEventCountDTO> identifierParamValueToCountMap = statEventParamLogProvider.countParamLogs(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), identifierParam.getParamKey(), minTime, maxTime);

        List<StatEventStatistic> statList = new ArrayList<>();
        // map: {参数值:次数}
        for (Map.Entry<String, StatEventCountDTO> entry : identifierParamValueToCountMap.entrySet()) {

            StatEventCountDTO count = entry.getValue();

            // 拿到底部导航栏的配置config
            StatEventPortalConfig portalConfig = statEventPortalConfigProvider.findPortalConfig(
                    namespace.getId(), getConfigType().getCode(), entry.getKey());
            if (portalConfig == null) {
                continue;
            }

            // 根据配置名称拿到底部导航栏的今天的统计数据
            StatEventPortalStatistic portalStat = statEventPortalStatisticProvider.findStatEventPortalStatistic(
                    namespace.getId(), getStatType().getCode(), portalConfig.getConfigName(), date);
            if (portalStat == null) {
                continue;
            }

            StatEventStatistic eventStat = getEventStat(entry.getKey(), portalStat);
            if (eventStat == null) {
                continue;
            }

            eventStat.setStatDate(date);
            eventStat.setNamespaceId(namespace.getId());

            eventStat.setTotalCount(count.getTotalCount().longValue());
            eventStat.setUniqueUsers(count.getUniqueUsers().longValue());
            eventStat.setCompletedSessions(count.getCompletedSessions().longValue());

            eventStat.setEventName(statEvent.getEventName());
            eventStat.setEventPortalStatId(portalStat.getId());
            eventStat.setEventDisplayName(statEvent.getEventDisplayName());

            eventStat.setEventType(statEvent.getEventType());
            eventStat.setEventVersion(statEvent.getEventVersion());
            eventStat.setTimeInterval(interval.getCode());

            eventStat.setOwnerType(EhStatEventPortalConfigs.class.getSimpleName());
            eventStat.setOwnerId(portalConfig.getId());

            eventStat.setEventName(statEvent.getEventName());

            statList.add(eventStat);
        }
        return statList;
    }

    @Override
    public List<StatEventParamLog> processEventParamLogs(StatEventLog log, Map<String, String> param) {
        List<StatEventParamLog> paramLogs = new ArrayList<>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            StatEventParam statEventParam = statEventParamProvider.findStatEventParam(log.getEventName(), entry.getKey());
            if (statEventParam != null) {
                StatEventParamLog paramLog = new StatEventParamLog();
                paramLog.setStatus(StatEventCommonStatus.ACTIVE.getCode());
                paramLog.setSessionId(log.getSessionId());
                paramLog.setNamespaceId(log.getNamespaceId());
                paramLog.setEventType(log.getEventType());
                paramLog.setEventName(log.getEventName());
                paramLog.setUid(log.getUid());
                paramLog.setEventLogId(log.getId());
                paramLog.setParamKey(entry.getKey());
                paramLog.setEventVersion(log.getEventVersion());
                paramLog.setUploadTime(log.getUploadTime());
                if (statEventParam.getParamType() == StatEventParamType.NUMBER.getCode()) {
                    paramLog.setNumberValue(Integer.valueOf(entry.getValue()));
                } else {
                    paramLog.setStringValue(entry.getValue());
                }
                paramLogs.add(paramLog);
            }
        }
        return paramLogs;
    }

    abstract protected StatEventPortalStatType getStatType();

    abstract protected StatEventPortalConfigType getConfigType();

    abstract protected StatEventStatistic getEventStat(String identifierParamsValue, StatEventPortalStatistic portalStat);

    abstract protected StatEventParam getIdentifierParam(List<StatEventParam> params);
}
