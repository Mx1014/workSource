package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>groups: 组列表 {@link com.everhomes.rest.flow.FlowConditionVariableGroup}</li>
 * </ul>
 */
public class ListFlowConditionVariablesResponse {

    @ItemType(FlowConditionVariableGroup.class)
    private List<FlowConditionVariableGroup> groups;

    public List<FlowConditionVariableGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<FlowConditionVariableGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
