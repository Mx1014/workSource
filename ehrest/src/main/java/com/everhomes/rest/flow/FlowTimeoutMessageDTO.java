package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowTimeoutMessageDTO {
	private Long flowCaseId;
	private Long flowNodeId;
	private Long flowTargetId;
	private Long flowMainId;
	private Integer flowVersion;
	private Long stepCount;
	private Long timeoutAtTick;
	private Long remindTick;
	private Long remindCount;
	private Long operatorId;

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Long getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public Long getFlowMainId() {
		return flowMainId;
	}

	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Long getStepCount() {
		return stepCount;
	}

	public void setStepCount(Long stepCount) {
		this.stepCount = stepCount;
	}
	
	public Long getTimeoutAtTick() {
		return timeoutAtTick;
	}

	public void setTimeoutAtTick(Long timeoutAtTick) {
		this.timeoutAtTick = timeoutAtTick;
	}

	public Long getRemindTick() {
		return remindTick;
	}

	public void setRemindTick(Long remindTick) {
		this.remindTick = remindTick;
	}

	public Long getRemindCount() {
		return remindCount;
	}

	public void setRemindCount(Long remindCount) {
		this.remindCount = remindCount;
	}

	public Long getFlowTargetId() {
		return flowTargetId;
	}

	public void setFlowTargetId(Long flowTargetId) {
		this.flowTargetId = flowTargetId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
