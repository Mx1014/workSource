// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <Li>processorButtons: 处理人按钮列表{@link com.everhomes.rest.flow.FlowButtonDTO}</Li>
 *     <Li>applierButtons: 申请人按钮列表{@link com.everhomes.rest.flow.FlowButtonDTO}</Li>
 * </ul>
 */
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
