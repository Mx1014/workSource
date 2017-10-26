package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowUserType: 目前该参数没用 {@link com.everhomes.rest.flow.FlowUserType}</li>
 *     <li>moduleId: 模块id</li>
 * </ul>
 */
public class ListFlowServiceTypesCommand {

    private String flowUserType;
    private Long moduleId;

    public String getFlowUserType() {
        return flowUserType;
    }

    public void setFlowUserType(String flowUserType) {
        this.flowUserType = flowUserType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
