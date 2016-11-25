package com.everhomes.flow;

import java.util.List;

import com.everhomes.rest.flow.FlowStepType;

public abstract class FlowGraphNode {
	private FlowNode flowNode;
	private List<FlowGraphButton> processorButtons;
	private List<FlowGraphButton> applierButtons;
	
	private List<FlowGraphAction> enterActions;
	private List<FlowGraphAction> leaveActions;
	private List<FlowGraphAction> timeoutActions;
	
	public abstract FlowStepType getStepType();
	public abstract void stepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException;
	public abstract void stepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException;
	public abstract void onAction(FlowCaseState ctx, FlowGraphEvent event);
	
	public FlowNode getFlowNode() {
		return flowNode;
	}
	public void setFlowNode(FlowNode flowNode) {
		this.flowNode = flowNode;
	}
	public List<FlowGraphButton> getProcessorButtons() {
		return processorButtons;
	}
	public void setProcessorButtons(List<FlowGraphButton> processorButtons) {
		this.processorButtons = processorButtons;
	}
	public List<FlowGraphButton> getApplierButtons() {
		return applierButtons;
	}
	public void setApplierButtons(List<FlowGraphButton> applierButtons) {
		this.applierButtons = applierButtons;
	}
	public List<FlowGraphAction> getEnterActions() {
		return enterActions;
	}
	public void setEnterActions(List<FlowGraphAction> enterActions) {
		this.enterActions = enterActions;
	}
	public List<FlowGraphAction> getLeaveActions() {
		return leaveActions;
	}
	public void setLeaveActions(List<FlowGraphAction> leaveActions) {
		this.leaveActions = leaveActions;
	}
	public List<FlowGraphAction> getTimeoutActions() {
		return timeoutActions;
	}
	public void setTimeoutActions(List<FlowGraphAction> timeoutActions) {
		this.timeoutActions = timeoutActions;
	}
}
