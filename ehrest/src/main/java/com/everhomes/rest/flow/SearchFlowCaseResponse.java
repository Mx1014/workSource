package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchFlowCaseResponse {
	@ItemType(FlowCaseDTO.class)
	private List<FlowCaseDTO> flowCases;
	private Long nextPageAnchor;

	public List<FlowCaseDTO> getFlowCases() {
		return flowCases;
	}

	public void setFlowCases(List<FlowCaseDTO> flowCases) {
		this.flowCases = flowCases;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
