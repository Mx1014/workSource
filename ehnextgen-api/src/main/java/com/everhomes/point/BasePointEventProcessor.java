package com.everhomes.point;

import com.everhomes.bus.LocalEvent;

import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/12/7.
 */
public interface BasePointEventProcessor {

    String[] init();

    PointEventProcessResult execute(LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory);

    List<PointResultAction> getResultActions(List<PointAction> pointActions, LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory category);

    List<PointRule> getPointRules(String eventName);

    void hook(LocalEvent localEvent, String subscriptionPath, PointEventGroup eventGroup);

    boolean isContinue(BasePointEventProcessor eventProcessor);

    PointEventGroup getEventGroup(Map<String, PointEventGroup> eventNameToPointEventGroupMap, LocalEvent localEvent, String subscriptionPath);
}
