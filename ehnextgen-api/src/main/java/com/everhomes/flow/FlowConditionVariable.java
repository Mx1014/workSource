package com.everhomes.flow;

/**
 * 变量比较器
 */
public interface FlowConditionVariable<T> {

    T getVariable();

    boolean isEqual(FlowConditionVariable variable);

    boolean isGreaterThen(FlowConditionVariable variable);

    boolean isLessThen(FlowConditionVariable variable);
}