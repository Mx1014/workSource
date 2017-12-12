package com.everhomes.flow;

/**
 * 条件计算器
 */
public interface FlowModuleConditionEvaluator {

    /**
     * 等于比较
     */
    boolean evaluateEqual(FlowCaseState ctx, FlowConditionExpression exp);

    /**
     * 大于比较
     */
    boolean evaluateGreaterThen(FlowCaseState ctx, FlowConditionExpression exp);

    /**
     * 包含比较
     */
    boolean evaluateContain(FlowCaseState ctx, FlowConditionExpression exp);
}