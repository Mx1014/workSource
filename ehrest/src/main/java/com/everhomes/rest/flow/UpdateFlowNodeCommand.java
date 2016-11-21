package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNodeCommand {
	private Long flowNodeId;
	private String flowNodeName;
	private Long autoStepMinute;
	private Byte allowApplierUpdate;

	public Long getAutoStepMinute() {
		return autoStepMinute;
	}
	public void setAutoStepMinute(Long autoStepMinute) {
		this.autoStepMinute = autoStepMinute;
	}
	public Byte getAllowApplierUpdate() {
		return allowApplierUpdate;
	}
	public void setAllowApplierUpdate(Byte allowApplierUpdate) {
		this.allowApplierUpdate = allowApplierUpdate;
	}
	public Long getFlowNodeId() {
		return flowNodeId;
	}
	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}
	public String getFlowNodeName() {
		return flowNodeName;
	}
	public void setFlowNodeName(String flowNodeName) {
		this.flowNodeName = flowNodeName;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }	
}
