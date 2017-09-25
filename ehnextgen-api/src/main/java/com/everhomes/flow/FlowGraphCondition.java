package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

public abstract class FlowGraphCondition {

    protected FlowCondition condition;
    protected List<FlowConditionExpression> expressions;

    public FlowGraphCondition() {
        expressions = new ArrayList<>();
    }

    public FlowCondition getCondition() {
        return condition;
    }

    public void setCondition(FlowCondition condition) {
        this.condition = condition;
    }

    public List<FlowConditionExpression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<FlowConditionExpression> expressions) {
        this.expressions = expressions;
    }

    abstract public boolean isTrue(FlowCaseState ctx) throws FlowStepErrorException;
}
