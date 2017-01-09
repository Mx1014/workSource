package com.everhomes.rest.flow;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListFlowUserSelectionResponse {
	@ItemType(FlowUserSelectionDTO.class)
	private List<FlowUserSelectionDTO> selections;
	
	public ListFlowUserSelectionResponse() {
		selections = new ArrayList<FlowUserSelectionDTO>();
	}

	public List<FlowUserSelectionDTO> getSelections() {
		return selections;
	}

	public void setSelections(List<FlowUserSelectionDTO> selections) {
		this.selections = selections;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
