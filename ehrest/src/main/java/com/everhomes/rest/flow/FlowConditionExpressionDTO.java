package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>flowConditionId: flowConditionId</li>
 *     <li>logicOperator: 逻辑运算符, &&, ||, !</li>
 *     <li>relationalOperator: 关系运算符, >, <, ==, !=</li>
 *     <li>variableType1: 变量1的类型{@link com.everhomes.rest.flow.FlowConditionExpressionVarType}</li>
 *     <li>variable1: 变量1的值,可以是变量形:`${amount}`或者数字形式: 3</li>
 *     <li>variableType2: 变量2的类型{@link com.everhomes.rest.flow.FlowConditionExpressionVarType}</li>
 *     <li>variable2: 变量2的值,可以是变量形:`${amount}`或者数字形式: 3</li>
 * </ul>
 */
public class FlowConditionExpressionDTO {

    private Long id;
    private Integer namespaceId;
    private Long flowMainId;
    private Integer flowVersion;
    private Long flowConditionId;
    private String logicOperator;
    private String relationalOperator;
    private Byte variableType1;
    private String variable1;
    private Byte variableType2;
    private String variable2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Long getFlowConditionId() {
        return flowConditionId;
    }

    public void setFlowConditionId(Long flowConditionId) {
        this.flowConditionId = flowConditionId;
    }

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

    public Byte getVariableType1() {
        return variableType1;
    }

    public void setVariableType1(Byte variableType1) {
        this.variableType1 = variableType1;
    }

    public String getVariable1() {
        return variable1;
    }

    public void setVariable1(String variable1) {
        this.variable1 = variable1;
    }

    public Byte getVariableType2() {
        return variableType2;
    }

    public void setVariableType2(Byte variableType2) {
        this.variableType2 = variableType2;
    }

    public String getVariable2() {
        return variable2;
    }

    public void setVariable2(String variable2) {
        this.variable2 = variable2;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
