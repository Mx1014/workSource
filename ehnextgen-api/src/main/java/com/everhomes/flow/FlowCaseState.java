package com.everhomes.flow;

import java.util.Map;

import com.everhomes.rest.flow.FlowStepType;

public class FlowCaseState {
	private String moduleName;
	private FlowModuleInfo module;
	private FlowCase flowCase;
	private FlowGraph flowGraph;
	private FlowStepType stepType;
	private FlowGraphNode prefixNode;
	private FlowGraphNode currentNode;
	private FlowGraphNode nextNode;
	private FlowGraphEvent currentEvent;
	private Map<String, Object> extra;

	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public FlowModuleInfo getModule() {
		return module;
	}
	public void setModule(FlowModuleInfo module) {
		this.module = module;
	}
	public FlowCase getFlowCase() {
		return flowCase;
	}
	public void setFlowCase(FlowCase flowCase) {
		this.flowCase = flowCase;
	}
	public FlowGraph getFlowGraph() {
		return flowGraph;
	}
	public void setFlowGraph(FlowGraph flowGraph) {
		this.flowGraph = flowGraph;
	}
	public FlowGraphNode getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(FlowGraphNode currentNode) {
		this.currentNode = currentNode;
	}
	public FlowGraphNode getPrefixNode() {
		return prefixNode;
	}
	public void setPrefixNode(FlowGraphNode prefixNode) {
		this.prefixNode = prefixNode;
	}
	public FlowGraphNode getNextNode() {
		return nextNode;
	}
	public void setNextNode(FlowGraphNode nextNode) {
		this.nextNode = nextNode;
	}
	public FlowGraphEvent getCurrentEvent() {
		return currentEvent;
	}
	public void setCurrentEvent(FlowGraphEvent currentEvent) {
		this.currentEvent = currentEvent;
	}
	public Map<String, Object> getExtra() {
		return extra;
	}
	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}
	public FlowStepType getStepType() {
		return stepType;
	}
	public void setStepType(FlowStepType stepType) {
		this.stepType = stepType;
	}
}
