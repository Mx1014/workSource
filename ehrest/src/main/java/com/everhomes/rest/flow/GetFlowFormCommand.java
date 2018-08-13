package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entityType: 实体类型, 工作流: flow, flow_node</li>
 *     <li>entityId: 实体 id</li>
 * </ul>
 */
public class GetFlowFormCommand {

	private String entityType;
	private Long entityId;

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
