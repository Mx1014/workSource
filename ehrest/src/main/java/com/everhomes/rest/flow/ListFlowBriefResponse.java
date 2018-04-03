package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListFlowBriefResponse {
	private Long nextPageAnchor;
	
	@ItemType(FlowDTO.class)
	private List<FlowDTO> flows;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<FlowDTO> getFlows() {
		return flows;
	}

	public void setFlows(List<FlowDTO> flows) {
		this.flows = flows;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
