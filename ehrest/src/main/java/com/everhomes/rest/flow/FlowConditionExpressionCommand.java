package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>logicOperator: 逻辑运算符, &&, ||, !</li>
 *     <li>relationalOperator: 关系运算符, >, <, ==, !=</li>
 *     <li>variableType1: 变量1的类型{@link FlowConditionExpressionVarType}</li>
 *     <li>variable1: 变量1的值,可以是变量形:`${amount}`或者数字形式: 3</li>
 *     <li>variableExtra1: variable1Extra</li>
 *     <li>variableType2: 变量2的类型{@link FlowConditionExpressionVarType}</li>
 *     <li>variable2: 变量2的值,可以是变量形:`${amount}`或者数字形式: 3</li>
 *     <li>variableExtra2: variable2Extra</li>
 * </ul>
 */
public class FlowConditionExpressionCommand {

    private String logicOperator;
    private String relationalOperator;
    private String variableType1;
    private String variable1;
    private String variableExtra1;
    private String variableType2;
    private String variable2;
    private String variableExtra2;

    public String getLogicOperator() {
        return logicOperator;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    public String getRelationalOperator() {
        return relationalOperator;
    }

    public void setRelationalOperator(String relationalOperator) {
        this.relationalOperator = relationalOperator;
    }

    public String getVariable1() {
        return variable1;
    }

    public void setVariable1(String variable1) {
        this.variable1 = variable1;
    }

    public String getVariableType1() {
        return variableType1;
    }

    public void setVariableType1(String variableType1) {
        this.variableType1 = variableType1;
    }

    public String getVariableType2() {
        return variableType2;
    }

    public void setVariableType2(String variableType2) {
        this.variableType2 = variableType2;
    }

    public String getVariable2() {
        return variable2;
    }

    public void setVariable2(String variable2) {
        this.variable2 = variable2;
    }

    public String getVariableExtra1() {
        return variableExtra1;
    }

    public void setVariableExtra1(String variableExtra1) {
        this.variableExtra1 = variableExtra1;
    }

    public String getVariableExtra2() {
        return variableExtra2;
    }

    public void setVariableExtra2(String variableExtra2) {
        this.variableExtra2 = variableExtra2;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
