package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowId: the config flow id </li>
 * </ul>
 * @author janson
 *
 */
public class FlowIdCommand {
	Long flowId;

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
