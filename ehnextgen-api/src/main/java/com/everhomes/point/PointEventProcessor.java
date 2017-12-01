package com.everhomes.point;

/**
 * Created by xq.tian on 2017/12/7.
 */
public interface PointEventProcessor {

    String[] init();

    PointEventProcessResult execute(PointEventLog log, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory);
}
