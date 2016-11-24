package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class ListFlowUserSelectionCommand {
	private Long belongTo;
	private String flowEntityType;
	private String flowUserType;
	public Long getBelongTo() {
		return belongTo;
	}
	public void setBelongTo(Long belongTo) {
		this.belongTo = belongTo;
	}
	public String getFlowEntityType() {
		return flowEntityType;
	}
	public void setFlowEntityType(String flowEntityType) {
		this.flowEntityType = flowEntityType;
	}

	public String getFlowUserType() {
		return flowUserType;
	}
	public void setFlowUserType(String flowUserType) {
		this.flowUserType = flowUserType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
