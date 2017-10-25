package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>flowVars: 工作流变量 {@link com.everhomes.rest.flow.FlowVariableDTO}</li>
 *     <li>moduleVars: 自定义变量 {@link com.everhomes.rest.flow.FlowVariableDTO}</li>
 * </ul>
 */
public class FlowVariableResponse {

    @ItemType(FlowVariableDTO.class)
    private List<FlowVariableDTO> flowVars;

    @ItemType(FlowVariableDTO.class)
    private List<FlowVariableDTO> moduleVars;

    public List<FlowVariableDTO> getFlowVars() {
        return flowVars;
    }

    public void setFlowVars(List<FlowVariableDTO> flowVars) {
        this.flowVars = flowVars;
    }

    public List<FlowVariableDTO> getModuleVars() {
        return moduleVars;
    }

    public void setModuleVars(List<FlowVariableDTO> moduleVars) {
        this.moduleVars = moduleVars;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
