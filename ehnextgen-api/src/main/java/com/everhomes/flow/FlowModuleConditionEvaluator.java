package com.everhomes.flow;

/**
 * 条件计算器
 */
public interface FlowModuleConditionEvaluator {

    boolean evaluateEqual(FlowCaseState ctx, FlowConditionExpression exp);

    boolean evaluateGreaterThen(FlowCaseState ctx, FlowConditionExpression exp);

    boolean evaluateLessThen(FlowCaseState ctx, FlowConditionExpression exp);

    boolean evaluateCustomize(FlowCaseState ctx, FlowConditionExpression exp);

    boolean evaluateContains(FlowCaseState ctx, FlowConditionExpression exp);
}