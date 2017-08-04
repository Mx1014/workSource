package com.everhomes.statistics.event;

import com.everhomes.namespace.Namespace;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by xq.tian on 2017/7/31.
 */
public interface StatEventHandler {

    List<StatEventStatistic> process(Namespace namespace, StatEvent statEvent, LocalDate statDate);

    String getEventName();
}
