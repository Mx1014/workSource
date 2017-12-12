package com.everhomes.point;

import com.everhomes.bus.LocalEvent;

import java.util.List; /**
 * Created by xq.tian on 2017/12/7.
 */
public interface PointEventProcessor {

    String[] init();

    PointEventProcessResult execute(LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory);

    List<PointResultAction> getResultActions(List<PointAction> pointActions, LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory category);

    List<PointRule> getPointRules(PointSystem pointSystem, LocalEvent localEvent);
}
