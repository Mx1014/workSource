package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>entityId: 用户 ID 或者 选择人员的 ID。 </li>
 * <li>flowEntityType: 客户端对象选择下个节点的用户 ID 或者 用户选择类型 ID 。 {@link com.everhomes.rest.flow.FlowEntityType}</li>
 * </ul>
 * @author janson
 *
 */
public class FlowEntitySel {
	private Long entityId;
	private String flowEntityType;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getFlowEntityType() {
		return flowEntityType;
	}

	public void setFlowEntityType(String flowEntityType) {
		this.flowEntityType = flowEntityType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
