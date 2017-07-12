package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

public class FlowGraphButton {
	private FlowButton flowButton;
	private FlowGraphAction message;
	private FlowGraphAction sms;
	private List<FlowGraphAction> scripts;
	
	public FlowGraphButton() {
		scripts = new ArrayList<FlowGraphAction>();
	}

	public FlowGraphAction getMessage() {
		return message;
	}

	public void setMessage(FlowGraphAction message) {
		this.message = message;
	}

	public FlowGraphAction getSms() {
		return sms;
	}

	public void setSms(FlowGraphAction sms) {
		this.sms = sms;
	}

	public List<FlowGraphAction> getScripts() {
		return scripts;
	}

	public void setScripts(List<FlowGraphAction> scripts) {
		this.scripts = scripts;
	}

	public FlowButton getFlowButton() {
		return flowButton;
	}

	public void setFlowButton(FlowButton flowButton) {
		this.flowButton = flowButton;
	}
	
}
