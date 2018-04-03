package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ListBriefFlowNodeResponse {

	@ItemType(FlowNodeDTO.class)
	private List<FlowNodeDTO> flowNodes;

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
