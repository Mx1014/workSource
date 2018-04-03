// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>variableType: 变量类型 {@link com.everhomes.rest.flow.FlowConditionExpressionVarType}</li>
 * </ul>
 */
public class ListFlowConditionVariablesCommand {

    private Long flowId;
    private String variableType;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
