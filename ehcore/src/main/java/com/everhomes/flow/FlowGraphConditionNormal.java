package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.rest.flow.FlowConditionExpressionVarType;
import com.everhomes.rest.flow.FlowConditionLogicOperatorType;
import com.everhomes.rest.flow.FlowConditionRelationalOperatorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xq.tian on 2017/9/19.
 */
public class FlowGraphConditionNormal extends FlowGraphCondition {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowGraphConditionNormal.class);

    private FlowListenerManager listenerManager;
    private FlowService flowService;

    public FlowGraphConditionNormal() {
        listenerManager = PlatformContext.getComponent(FlowListenerManager.class);
        flowService = PlatformContext.getComponent(FlowService.class);
    }

    public boolean isTrue(FlowCaseState ctx) throws FlowStepErrorException {
        if (expressions == null) {
            return false;
        }
        if (expressions.size() == 0) {
            return true;
        }

        boolean conditionSuccess = false;
        for (FlowConditionExpression exp : expressions) {
            boolean expRetVal = evaluateFlowConditionVariableRelational(ctx, exp);
            FlowConditionLogicOperatorType logicOperatorType = FlowConditionLogicOperatorType.fromCode(exp.getLogicOperator());
            if (logicOperatorType == FlowConditionLogicOperatorType.OR && expRetVal) {
                conditionSuccess = true;
                break;
            } else if (logicOperatorType == FlowConditionLogicOperatorType.AND && !expRetVal){
                conditionSuccess = false;
                break;
            }
            conditionSuccess = expRetVal;
        }
        return conditionSuccess;
    }

    public boolean evaluateFlowConditionVariableRelational(FlowCaseState ctx, FlowConditionExpression exp) {
        FlowConditionVariable variable1 = getVariableValue(ctx, exp.getVariableType1(), exp.getVariable1(), exp.getVariableExtra1());
        FlowConditionVariable variable2 = getVariableValue(ctx, exp.getVariableType2(), exp.getVariable2(), exp.getVariableExtra2());

        if (variable1 == null || variable2 == null) {
            return false;
            // throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONDITION_VARIABLE,
            //         "Flow condition variable is null, exp = %s", exp);
        }

        FlowConditionRelationalOperatorType operatorType = FlowConditionRelationalOperatorType.fromCode(exp.getRelationalOperator());
        switch (operatorType) {
            case EQUAL:
                return variable1.isEqual(variable2);
            case NOT_EQUAL:
                return !variable1.isEqual(variable2);
            case GREATER_THEN:
                return variable1.isGreaterThen(variable2);
            case LESS_THEN:
                return variable1.isLessThen(variable2);
            case GREATER_OR_EQUAL:
                return variable1.isGreaterThen(variable2) || variable1.isEqual(variable2);
            case LESS_OR_EQUAL:
                return variable1.isLessThen(variable2) || variable1.isEqual(variable2);
        }
        return false;
    }

    private FlowConditionVariable getVariableValue(FlowCaseState ctx, String variableType, String variable, String extra) {
        FlowConditionExpressionVarType varType = FlowConditionExpressionVarType.fromCode(variableType);
        FlowConditionVariable value = null;
        switch (varType) {
            case CONST:
            case OPTION:
                value = new FlowConditionStringVariable(variable);
                break;
            case FORM:
                value = flowService.getFormFieldValueByVariable(ctx, variable, extra);
                break;
            case VARIABLE:
                value = listenerManager.onFlowConditionVariableRender(ctx, variable, extra);
                break;
        }
        return value;
    }
}
