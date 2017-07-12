package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowStepType;

public abstract class FlowGraphNode {
	private FlowNode flowNode;
	
	private FlowGraphAction messageAction;
	private FlowGraphAction smsAction;
	private FlowGraphAction tickMessageAction;
	private FlowGraphAction tickSMSAction;
	private FlowGraphAction trackApproveEnter;
	private FlowGraphAction trackRejectEnter;
	private FlowGraphAction trackTransferLeave;
	
	private List<FlowGraphButton> processorButtons;
	private List<FlowGraphButton> applierButtons;
	
	private List<FlowGraphAction> enterActions;
	private List<FlowGraphAction> leaveActions;
	private List<FlowGraphAction> timeoutActions;
	
	public FlowGraphNode() {
		processorButtons = new ArrayList<FlowGraphButton>();
		applierButtons = new ArrayList<FlowGraphButton>();
		enterActions = new ArrayList<FlowGraphAction>();
		leaveActions = new ArrayList<FlowGraphAction>();
		timeoutActions = new ArrayList<FlowGraphAction>();
	}
	
	public abstract void stepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException;
	public abstract void stepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException;
	public FlowCaseStatus getExpectStatus() {
		//TODO better for this
		FlowNode fn = this.flowNode;
		if(fn.getNodeName().equals("START")) {
			return FlowCaseStatus.INITIAL;
		} else if(fn.getNodeName().equals("END")) {
			return FlowCaseStatus.FINISHED;
		} else {
			return FlowCaseStatus.PROCESS;	
		}
	}
	
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
	public FlowGraphAction getMessageAction() {
		return messageAction;
	}
	public void setMessageAction(FlowGraphAction messageAction) {
		this.messageAction = messageAction;
	}
	public FlowGraphAction getSmsAction() {
		return smsAction;
	}
	public void setSmsAction(FlowGraphAction smsAction) {
		this.smsAction = smsAction;
	}
	public FlowGraphAction getTickMessageAction() {
		return tickMessageAction;
	}
	public void setTickMessageAction(FlowGraphAction tickMessageAction) {
		this.tickMessageAction = tickMessageAction;
	}
	public FlowGraphAction getTickSMSAction() {
		return tickSMSAction;
	}
	public void setTickSMSAction(FlowGraphAction tickSMSAction) {
		this.tickSMSAction = tickSMSAction;
	}
	public FlowGraphAction getTrackApproveEnter() {
		return trackApproveEnter;
	}
	public void setTrackApproveEnter(FlowGraphAction trackApproveEnter) {
		this.trackApproveEnter = trackApproveEnter;
	}
	public FlowGraphAction getTrackRejectEnter() {
		return trackRejectEnter;
	}
	public void setTrackRejectEnter(FlowGraphAction trackRejectEnter) {
		this.trackRejectEnter = trackRejectEnter;
	}
	public FlowGraphAction getTrackTransferLeave() {
		return trackTransferLeave;
	}
	public void setTrackTransferLeave(FlowGraphAction trackTransferLeave) {
		this.trackTransferLeave = trackTransferLeave;
	}
}
