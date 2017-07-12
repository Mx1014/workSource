package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowNodeTrackerDTO {	
	FlowActionDTO enterTracker;
	FlowActionDTO rejectTracker;
	FlowActionDTO transferTracker;

	public FlowActionDTO getEnterTracker() {
		return enterTracker;
	}

	public void setEnterTracker(FlowActionDTO enterTracker) {
		this.enterTracker = enterTracker;
	}

	public FlowActionDTO getRejectTracker() {
		return rejectTracker;
	}

	public void setRejectTracker(FlowActionDTO rejectTracker) {
		this.rejectTracker = rejectTracker;
	}

	public FlowActionDTO getTransferTracker() {
		return transferTracker;
	}

	public void setTransferTracker(FlowActionDTO transferTracker) {
		this.transferTracker = transferTracker;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
