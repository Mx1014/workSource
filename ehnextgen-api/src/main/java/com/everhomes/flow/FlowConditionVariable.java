package com.everhomes.flow;

import com.everhomes.rest.flow.FlowVariableCompareResult;

/**
 * 变量比较器
 */
public interface FlowConditionVariable<T> {

    T getVariable();

    boolean isEqual(FlowConditionVariable variable);

    boolean isGreaterThen(FlowConditionVariable variable);

    boolean isLessThen(FlowConditionVariable variable);

    // FlowVariableCompareResult compareTo(FlowConditionVariable variable);

    default FlowVariableCompareResult doResult(float result) {
        if (result == 0) {
            return FlowVariableCompareResult.EQUAL;
        } else if (result > 0) {
            return FlowVariableCompareResult.GREATER_THEN;
        } else {
            return FlowVariableCompareResult.LESS_THEN;
        }
    }
}