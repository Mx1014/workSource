package com.everhomes.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraph {
	private Flow flow;
	private List<FlowGraphNode> nodes;

	private Map<Long, FlowGraphNode> idToNode;
	private Map<Long, FlowGraphButton> idToButton;
	private Map<Long, FlowGraphAction> idToAction;
	
	public FlowGraph() {
		nodes = new ArrayList<FlowGraphNode>();
		idToNode = new HashMap<Long, FlowGraphNode>();
		idToButton = new HashMap<Long, FlowGraphButton>();
		idToAction = new HashMap<Long, FlowGraphAction>();
	}
	
	public Flow getFlow() {
		return flow;
	}
	public void setFlow(Flow flow) {
		this.flow = flow;
	}
	public List<FlowGraphNode> getNodes() {
		return nodes;
	}
	public Map<Long, FlowGraphNode> getIdToNode() {
		return idToNode;
	}
	public Map<Long, FlowGraphButton> getIdToButton() {
		return idToButton;
	}
	public Map<Long, FlowGraphAction> getIdToAction() {
		return idToAction;
	}
	public FlowGraphNode getGraphNode(Long id) {
		return idToNode.get(id);
	}
	public FlowGraphButton getGraphButton(Long id) {
		return idToButton.get(id);
	}
}
