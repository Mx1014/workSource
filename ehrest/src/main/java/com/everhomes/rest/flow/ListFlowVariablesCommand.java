package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowVariableType: {@link com.everhomes.rest.flow.FlowVariableType}</li>
 *     <li>entityType: entityType</li>
 *     <li>entityId: entityId</li>
 *     <li>keyword: keyword</li>
 * </ul>
 */
public class ListFlowVariablesCommand {

    private Integer namespaceId;
    private String flowVariableType;
    private String entityType;
    private Long entityId;
    private String keyword;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getFlowVariableType() {
        return flowVariableType;
    }

    public void setFlowVariableType(String flowVariableType) {
        this.flowVariableType = flowVariableType;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
