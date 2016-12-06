package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNodeCommand {
	private Long flowNodeId;
	private String flowNodeName;
	private Integer autoStepMinute;
	private Byte allowApplierUpdate;
	private String autoStepType;
	private String params;

	public String getAutoStepType() {
		return autoStepType;
	}
	public void setAutoStepType(String autoStepType) {
		this.autoStepType = autoStepType;
	}
	public Integer getAutoStepMinute() {
		return autoStepMinute;
	}
	public void setAutoStepMinute(Integer autoStepMinute) {
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

	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }	
}
