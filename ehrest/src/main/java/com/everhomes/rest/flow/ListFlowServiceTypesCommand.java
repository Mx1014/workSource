package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowUserType: 目前该参数没用 {@link com.everhomes.rest.flow.FlowUserType}</li>
 * </ul>
 */
public class ListFlowServiceTypesCommand {

    private String flowUserType;

    public String getFlowUserType() {
        return flowUserType;
    }

    public void setFlowUserType(String flowUserType) {
        this.flowUserType = flowUserType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
