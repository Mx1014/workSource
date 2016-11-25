package com.everhomes.flow;

import java.util.List;
import java.util.Map;

public class FlowGraph {
	private Flow flow;
	private List<FlowGraphNode> nodes;

	private Map<Long, FlowGraphNode> idToNode;
	private Map<Long, FlowGraphButton> idToButton;
	private Map<Long, FlowGraphAction> idToAction;
}
