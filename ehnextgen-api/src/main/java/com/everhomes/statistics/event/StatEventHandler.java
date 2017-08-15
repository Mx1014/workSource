package com.everhomes.statistics.event;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by xq.tian on 2017/7/31.
 */
public interface StatEventHandler {

    List<StatEventStatistic> process(Namespace namespace, StatEvent statEvent, LocalDate statDate, StatEventStatTimeInterval interval);

    String getEventName();
}
