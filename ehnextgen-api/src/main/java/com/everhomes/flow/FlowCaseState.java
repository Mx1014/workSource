package com.everhomes.flow;

import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class FlowCaseState {
	private FlowModuleInfo module;
	private FlowCase flowCase;
	private UserInfo operator;
	private FlowGraph flowGraph;
	private FlowStepType stepType;
	private FlowGraphNode prefixNode;
	private FlowGraphNode currentNode;
	private FlowGraphNode nextNode;
	private FlowGraphEvent currentEvent;
	private Map<String, Object> extra;
	private List<FlowEventLog> logs;
	private List<FlowEventLog> updateLogs;
	private List<FlowTimeout> timeouts;
	private Stack<FlowCaseStateStackType> processStack;
	
	public FlowCaseState() {
		extra = new ConcurrentHashMap<String, Object>();
		logs = new ArrayList<FlowEventLog>();
		timeouts = new ArrayList<FlowTimeout>();
		updateLogs = new ArrayList<FlowEventLog>();
		processStack = new Stack<FlowCaseStateStackType>(); 
	}

	public String getModuleName() {
		if(this.module != null) {
			return this.module.getModuleName();
		}
		
		return null;
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
	public FlowStepType getStepType() {
		return stepType;
	}
	public void setStepType(FlowStepType stepType) {
		this.stepType = stepType;
	}

	public List<FlowEventLog> getLogs() {
		return logs;
	}
	
	public List<FlowTimeout> getTimeouts() {
		return timeouts;
	}

	public UserInfo getOperator() {
		return operator;
	}

	public void setOperator(UserInfo operator) {
		this.operator = operator;
	}

	public void addUpdateLogs(FlowEventLog updateLog) {
		this.updateLogs.add(updateLog);
	}

	public List<FlowEventLog> getUpdateLogs() {
		return updateLogs;
	}

	public void pushProcessType(FlowCaseStateStackType processType) {
		processStack.push(processType);
	}
	
	public FlowCaseStateStackType popProcessType() {
		return processStack.pop();
	}
	
	public FlowCaseStateStackType peekProcessType() {
		return processStack.peek();
	}
	
	public FlowCaseStateStackType firstProcessType() {
		return processStack.firstElement();
	}
}
