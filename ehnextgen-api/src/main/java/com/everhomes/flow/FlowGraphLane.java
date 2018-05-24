package com.everhomes.flow;

import java.io.Serializable;

public class FlowGraphLane implements Serializable {

	private FlowLane flowLane;

    public FlowLane getFlowLane() {
        return flowLane;
    }

    public void setFlowLane(FlowLane flowLane) {
        this.flowLane = flowLane;
    }
}
