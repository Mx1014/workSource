package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>stepCount: 当前跳转的次数，通过这个值可以避免多次跳转</li>
 * <li>autoStepType: {@link com.everhomes.rest.flow.FlowStepType}</li>
 * </ul>
 * @author janson
 *
 */
public class FlowAutoStepDTO {
	private Long flowCaseId;
	private Long flowNodeId;
	private Long flowMainId;
	private Integer flowVersion;
	private Long stepCount;
	private String autoStepType;
	private Long flowTargetId;
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

	public String getAutoStepType() {
		return autoStepType;
	}

	public void setAutoStepType(String autoStepType) {
		this.autoStepType = autoStepType;
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
