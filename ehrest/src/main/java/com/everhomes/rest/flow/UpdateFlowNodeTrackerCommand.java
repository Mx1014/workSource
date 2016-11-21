package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNodeTrackerCommand {
	private Long flowNodeId;
	
	UpdateFlowNodeSingleTracker enterTracker;
	UpdateFlowNodeSingleTracker rejectTracker;
	UpdateFlowNodeSingleTracker transferTracker;

	public Long getFlowNodeId() {
		return flowNodeId;
	}

	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}

	public UpdateFlowNodeSingleTracker getEnterTracker() {
		return enterTracker;
	}

	public void setEnterTracker(UpdateFlowNodeSingleTracker enterTracker) {
		this.enterTracker = enterTracker;
	}

	public UpdateFlowNodeSingleTracker getRejectTracker() {
		return rejectTracker;
	}

	public void setRejectTracker(UpdateFlowNodeSingleTracker rejectTracker) {
		this.rejectTracker = rejectTracker;
	}

	public UpdateFlowNodeSingleTracker getTransferTracker() {
		return transferTracker;
	}

	public void setTransferTracker(UpdateFlowNodeSingleTracker transferTracker) {
		this.transferTracker = transferTracker;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
