package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowPostEvaluateCommand {
	private Byte     star;
	private Long     userId;
	private Long     flowCaseId;
	private Long flowNodeId;
	
	public Byte getStar() {
		return star;
	}
	public void setStar(Byte star) {
		this.star = star;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
