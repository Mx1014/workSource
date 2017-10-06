package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowConditionExpressionVarType;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/9/19.
 */
public class FlowGraphConditionNormal extends FlowGraphCondition {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowGraphConditionNormal.class);

    private ScriptEngineFactory scriptEngineFactory;
    private FlowListenerManager listenerManager;

    public FlowGraphConditionNormal() {
        scriptEngineFactory = new NashornScriptEngineFactory();
        listenerManager = PlatformContext.getComponent(FlowListenerManager.class);
    }

    public boolean isTrue(FlowCaseState ctx) throws FlowStepErrorException {
        if (expressions != null && expressions.size() == 0) {
            return true;
        }

        List<String> vars = new ArrayList<>();
        for (FlowConditionExpression exp : expressions) {
            FlowConditionExpressionVarType expVarType = FlowConditionExpressionVarType.fromCode(exp.getVariableType1());
            if (expVarType == FlowConditionExpressionVarType.VARIABLE) {
                vars.add(exp.getVariable1());
            }
            expVarType = FlowConditionExpressionVarType.fromCode(exp.getVariableType2());
            if (expVarType == FlowConditionExpressionVarType.VARIABLE) {
                vars.add(exp.getVariable2());
            }
        }

        Map<String, String> varNameToValueMap = null;
        if (vars.size() > 0) {
            varNameToValueMap = listenerManager.onFlowVariableRender(ctx, vars);
        }
        if (varNameToValueMap == null) {
            return false;
        }

        StringBuilder sb = new StringBuilder(100);
        for (int i = 0; i < expressions.size(); i++) {
            FlowConditionExpression exp = expressions.get(i);

            String value1 = getVarValue(exp.getVariableType1(), exp.getVariable1(), varNameToValueMap);
            String value2 = getVarValue(exp.getVariableType2(), exp.getVariable2(), varNameToValueMap);

            sb.append(value1).append(exp.getRelationalOperator()).append(value2);
            if (i < expressions.size() - 1) {
                sb.append(exp.getLogicOperator());
            }
        }

        ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine();
        try {
            Object result = scriptEngine.eval(sb.toString());
            return Boolean.parseBoolean(result.toString());
        } catch (ScriptException e) {
            LOGGER.error("flow condition expression isTrue error, cond = {}", condition);
        }
        return false;
    }

    private String getVarValue(String varType, String variable, Map<String, String> varNameToValueMap) {
        FlowConditionExpressionVarType expVarType = FlowConditionExpressionVarType.fromCode(varType);
        String realValue = "";
        if (expVarType == FlowConditionExpressionVarType.VARIABLE) {
            realValue = varNameToValueMap.get(variable);
        } else if (expVarType == FlowConditionExpressionVarType.CONST) {
            realValue = variable;
        }
        return realValue;
    }
}
