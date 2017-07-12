package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNodeTrackerCommand {
	private Long flowNodeId;
	
	FlowActionInfo enterTracker;
	FlowActionInfo rejectTracker;
	FlowActionInfo transferTracker;

	public Long getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public FlowActionInfo getEnterTracker() {
		return enterTracker;
	}

	public void setEnterTracker(FlowActionInfo enterTracker) {
		this.enterTracker = enterTracker;
	}

	public FlowActionInfo getRejectTracker() {
		return rejectTracker;
	}

	public void setRejectTracker(FlowActionInfo rejectTracker) {
		this.rejectTracker = rejectTracker;
	}

	public FlowActionInfo getTransferTracker() {
		return transferTracker;
	}

	public void setTransferTracker(FlowActionInfo transferTracker) {
		this.transferTracker = transferTracker;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
