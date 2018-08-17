package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>name: 名称</li>
 *     <li>entityType: entityType</li>
 *     <li>entityId: entityId</li>
 *     <li>variables: 变量列表 {@link com.everhomes.rest.flow.FlowConditionVariableDTO}</li>
 * </ul>
 */
public class FlowConditionVariableGroup {

    private String name;
    private String entityType;
    private Long entityId;

    private List<FlowConditionVariableDTO> variables;

    public FlowConditionVariableGroup(String name, String entityType, Long entityId, List<FlowConditionVariableDTO> variables) {
        this.name = name;
        this.entityType = entityType;
        this.entityId = entityId;
        this.variables = variables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

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
