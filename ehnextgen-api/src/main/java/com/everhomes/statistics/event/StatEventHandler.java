package com.everhomes.statistics.event;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/7/31.
 */
public interface StatEventHandler {

    List<StatEventStatistic> processStat(Namespace namespace, StatEvent statEvent, LocalDate statDate, StatEventStatTimeInterval interval);

    String getEventName();

    List<StatEventParamLog> processEventParamLogs(StatEventLog log, Map<String, String> param);
}
