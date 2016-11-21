package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class CreateFlowUserSelectionCommand {
	private Long belongTo;
	private String flowUserBelongType;
	private String flowEntityType;
	
	@ItemType(FlowSingleUserSelectionCommand.class)
	List<FlowSingleUserSelectionCommand> selections;

	public Long getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(Long belongTo) {
		this.belongTo = belongTo;
	}

	public String getFlowUserBelongType() {
		return flowUserBelongType;
	}

	public void setFlowUserBelongType(String flowUserBelongType) {
		this.flowUserBelongType = flowUserBelongType;
	}

	public String getFlowEntityType() {
		return flowEntityType;
	}

	public void setFlowEntityType(String flowEntityType) {
		this.flowEntityType = flowEntityType;
	}

	public List<FlowSingleUserSelectionCommand> getSelections() {
		return selections;
	}

	public void setSelections(List<FlowSingleUserSelectionCommand> selections) {
		this.selections = selections;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
