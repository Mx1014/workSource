package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class UpdateFlowEvaluateCommand {
	private Long flowId;
	private Byte needEvaluate;
	private Long evaluateStart;
	private Long evaluateEnd;
	private String evaluateStep;
	
	private FlowActionInfo messageAction;
	private FlowActionInfo smsAction;
	
	@ItemType(String.class)
	private List<String> items;

	public Byte getNeedEvaluate() {
		return needEvaluate;
	}

	public void setNeedEvaluate(Byte needEvaluate) {
		this.needEvaluate = needEvaluate;
	}

	public Long getEvaluateStart() {
		return evaluateStart;
	}

	public void setEvaluateStart(Long evaluateStart) {
		this.evaluateStart = evaluateStart;
	}

	public Long getEvaluateEnd() {
		return evaluateEnd;
	}

	public void setEvaluateEnd(Long evaluateEnd) {
		this.evaluateEnd = evaluateEnd;
	}

	public String getEvaluateStep() {
		return evaluateStep;
	}

	public void setEvaluateStep(String evaluateStep) {
		this.evaluateStep = evaluateStep;
	}

	public FlowActionInfo getMessageAction() {
		return messageAction;
	}

	public void setMessageAction(FlowActionInfo messageAction) {
		this.messageAction = messageAction;
	}

	public FlowActionInfo getSmsAction() {
		return smsAction;
	}

	public void setSmsAction(FlowActionInfo smsAction) {
		this.smsAction = smsAction;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
