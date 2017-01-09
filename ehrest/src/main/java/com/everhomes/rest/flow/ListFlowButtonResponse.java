package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListFlowButtonResponse {
	@ItemType(FlowButtonDTO.class)
	private List<FlowButtonDTO> processorButtons;
	
	@ItemType(FlowButtonDTO.class)
	private List<FlowButtonDTO> applierButtons;

	public List<FlowButtonDTO> getProcessorButtons() {
		return processorButtons;
	}

	public void setProcessorButtons(List<FlowButtonDTO> processorButtons) {
		this.processorButtons = processorButtons;
	}

	public List<FlowButtonDTO> getApplierButtons() {
		return applierButtons;
	}

	public void setApplierButtons(List<FlowButtonDTO> applierButtons) {
		this.applierButtons = applierButtons;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
