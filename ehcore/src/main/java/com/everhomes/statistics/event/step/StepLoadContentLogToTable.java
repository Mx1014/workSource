package com.everhomes.statistics.event.step;

import com.everhomes.db.DbProvider;
import com.everhomes.rest.statistics.event.StatEventCommonStatus;
import com.everhomes.rest.statistics.event.StatEventLogDTO;
import com.everhomes.rest.statistics.event.StatEventParamType;
import com.everhomes.statistics.event.*;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/8/14.
 */
@Component
public class StepLoadContentLogToTable extends AbstractStatEventStep {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private StatEventContentLogProvider statEventContentLogProvider;

    @Autowired
    private StatEventDeviceLogProvider statEventDeviceLogProvider;

    @Autowired
    private StatEventProvider statEventProvider;

    @Autowired
    private StatEventLogProvider statEventLogProvider;

    @Autowired
    private StatEventParamProvider statEventParamProvider;

    @Autowired
    private StatEventParamLogProvider statEventParamLogProvider;

    @Override
    public void doExecute(StatEventExecution execution) {
        LocalDate statDate = execution.getParam("statDate");

        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));

        List<StatEventLogContent> logContents = statEventContentLogProvider.listEventLogContent(StatEventCommonStatus.ACTIVE.getCode(), minTime, maxTime);

        Map<String, StatEventDeviceLog> statDeviceLogMap = new HashMap<>();
        Map<String, StatEvent> statEventMap = new HashMap<>();
        Map<String, StatEventParam> statEventParamMap = new HashMap<>();
        Map<String, List<Long>> sessionIdToDeviceGenIdMap = new HashMap<>();

        for (StatEventLogContent logContent : logContents) {
            StatEventLogDTO[] logs = (StatEventLogDTO[]) StringHelper.fromJsonString(logContent.getContent(), StatEventLogDTO[].class);
            dbProvider.execute(status -> {
                for (StatEventLogDTO logDTO : logs) {
                    if (logDTO == null) continue;

                    StatEventDeviceLog deviceLog = statDeviceLogMap.get(logDTO.getSessionId());
                    if (deviceLog == null) {
                        deviceLog = statEventDeviceLogProvider.findStatEventDeviceLogById(Long.valueOf(logDTO.getSessionId()));
                        statDeviceLogMap.put(String.valueOf(deviceLog.getId()), deviceLog);
                    }
                    StatEvent statEvent = statEventMap.get(logDTO.getEventName());
                    if (statEvent == null) {
                        statEvent = statEventProvider.findStatEventByName(logDTO.getEventName());
                        statEventMap.put(statEvent.getEventName(), statEvent);
                    }

                    // 根据deviceGenId去重
                    List<Long> deviceGenIdList = sessionIdToDeviceGenIdMap.get(logDTO.getSessionId());
                    if (deviceGenIdList == null) {
                        deviceGenIdList = statEventLogProvider.listDeviceGenIdBySessionId(logDTO.getSessionId());
                        deviceGenIdList.add(logDTO.getLogId());
                        sessionIdToDeviceGenIdMap.put(logDTO.getSessionId(), deviceGenIdList);
                    } else {
                        if (deviceGenIdList.contains(logDTO.getLogId())) {
                            continue;
                        }
                        deviceGenIdList.add(logDTO.getLogId());
                    }

                    StatEventLog log = new StatEventLog();
                    log.setNamespaceId(logContent.getNamespaceId());
                    log.setUid(deviceLog.getUid());
                    log.setDeviceGenId(logDTO.getLogId());
                    log.setAcc(logDTO.getAcc());
                    log.setDeviceGenId(logDTO.getLogId());
                    log.setEventName(logDTO.getEventName());
                    log.setEventVersion(logDTO.getVersion());
                    log.setEventType(statEvent.getEventType());
                    log.setSessionId(logDTO.getSessionId());
                    log.setStatus(StatEventCommonStatus.ACTIVE.getCode());
                    log.setDeviceTime(logDTO.getDeviceTime());
                    log.setAcc(1);
                    log.setUploadTime(logContent.getCreateTime());
                    statEventLogProvider.createStatEventLog(log);

                    Map<String, String> param = logDTO.getParam();
                    param.forEach((k, v) -> {
                        StatEventParam statEventParam = statEventParamMap.get(log.getEventName() + k);
                        if (statEventParam == null) {
                            statEventParam = statEventParamProvider.findStatEventParam(log.getEventName(), k);
                            statEventParamMap.put(log.getEventName() + k, statEventParam);
                        }
                        if (statEventParam != null) {
                            StatEventParamLog paramLog = new StatEventParamLog();
                            paramLog.setStatus(StatEventCommonStatus.ACTIVE.getCode());
                            paramLog.setSessionId(log.getSessionId());
                            paramLog.setNamespaceId(log.getNamespaceId());
                            paramLog.setEventType(log.getEventType());
                            paramLog.setEventName(log.getEventName());
                            paramLog.setUid(log.getUid());
                            paramLog.setEventLogId(log.getId());
                            paramLog.setParamKey(k);
                            paramLog.setEventVersion(log.getEventVersion());
                            paramLog.setUploadTime(logContent.getCreateTime());
                            if (statEventParam.getParamType() == StatEventParamType.NUMBER.getCode()) {
                                paramLog.setNumberValue(Integer.valueOf(v));
                            } else {
                                paramLog.setStringValue(v);
                            }
                            statEventParamLogProvider.createStatEventParamLog(paramLog);
                        }
                    });
                }
                return true;
            });
            // 把这条记录状态置为已load
            logContent.setStatus(StatEventCommonStatus.WAITING_FOR_CONFIRMATION.getCode());
            statEventContentLogProvider.updateStatEventLogContent(logContent);
        }
    }
}
