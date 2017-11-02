package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ListNextBranchesResponse {

	@ItemType(FlowNodeDTO.class)
	private List<FlowNodeDTO> nodes;

    public List<FlowNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<FlowNodeDTO> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
