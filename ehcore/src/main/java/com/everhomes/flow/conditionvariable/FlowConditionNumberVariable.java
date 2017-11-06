package com.everhomes.flow.conditionvariable;

import com.everhomes.flow.FlowConditionVariable;

/**
 * Created by xq.tian on 2017/11/2.
 */
public class FlowConditionNumberVariable implements FlowConditionVariable<Number> {

    private Number number;

    public FlowConditionNumberVariable(Number number) {
        this.number = number;
    }

    @Override
    public Number getVariable() {
        return number;
    }

    @Override
    public boolean isEqual(FlowConditionVariable variable) {
        return doCompare(this, variable, (number1, number2) -> Math.abs(number1.floatValue() - number2.floatValue()) < 0.0001);
    }

    @Override
    public boolean isGreaterThen(FlowConditionVariable variable) {
        return doCompare(this, variable, (number1, number2) -> Math.abs(number1.floatValue() - number2.floatValue()) > 0);
    }

    @Override
    public boolean isLessThen(FlowConditionVariable variable) {
        return doCompare(this, variable, (number1, number2) -> Math.abs(number1.floatValue() - number2.floatValue()) < 0);
    }

    interface CompareCallback {
        boolean compare(Number number1, Number number2);
    }

    private boolean doCompare(FlowConditionNumberVariable variable1, FlowConditionVariable variable2, CompareCallback callback) {
        if (variable2 instanceof FlowConditionNumberVariable) {
            return callback.compare(variable1.getVariable(), ((FlowConditionNumberVariable) variable2).getVariable());
        } else if (variable2 instanceof FlowConditionStringVariable) {
            FlowConditionNumberVariable numberVariable = ((FlowConditionStringVariable) variable2).toNumberVariable();
            if (numberVariable != null) {
                return callback.compare(variable1.getVariable(), numberVariable.getVariable());
            }
        }
        return false;
    }
}
