package com.everhomes.point;

import java.util.List; /**
 * Created by xq.tian on 2017/12/7.
 */
public interface PointEventProcessor {

    String[] init();

    PointEventProcessResult execute(PointEventLog log, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory);

    List<PointResultAction> getResultActions(List<PointAction> pointActions, PointEventLog log, PointRule rule, PointSystem pointSystem, PointRuleCategory category);
}
