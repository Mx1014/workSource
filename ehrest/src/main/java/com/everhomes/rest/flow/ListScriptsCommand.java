package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class ListScriptsCommand {
    private Integer     namespaceId;
    private String entityType;
    private Long entityId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
