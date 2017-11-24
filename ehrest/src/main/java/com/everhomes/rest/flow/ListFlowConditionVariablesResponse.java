package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>params: 参数列表 {@link FlowPredefinedParamDTO}</li>
 * </ul>
 */
public class ListFlowConditionVariablesResponse {

    @ItemType(FlowConditionVariableDTO.class)
    private List<FlowConditionVariableDTO> variables;

    public List<FlowConditionVariableDTO> getVariables() {
        return variables;
    }

    public void setVariables(List<FlowConditionVariableDTO> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
