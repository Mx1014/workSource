package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListBriefFlowNodeResponse {
	@ItemType(FlowNodeDTO.class)
	List<FlowNodeDTO> flowNodes;

	public List<FlowNodeDTO> getFlowNodes() {
		return flowNodes;
	}

	public void setFlowNodes(List<FlowNodeDTO> flowNodes) {
		this.flowNodes = flowNodes;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
