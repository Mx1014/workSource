package com.everhomes.flow;

public interface FlowSnapshotService {
	FlowGraph getFlowGraph(Long flowId, Integer flowVer);
}
