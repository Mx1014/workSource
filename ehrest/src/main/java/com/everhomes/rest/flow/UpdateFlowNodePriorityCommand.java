package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class UpdateFlowNodePriorityCommand {
	private Long flowMainId;
	
	@ItemType(FlowNodePriority.class)
	List<FlowNodePriority> flowNodes;

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public List<FlowNodePriority> getFlowNodes() {
		return flowNodes;
	}

	public void setFlowNodes(List<FlowNodePriority> flowNodes) {
		this.flowNodes = flowNodes;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }	
}
