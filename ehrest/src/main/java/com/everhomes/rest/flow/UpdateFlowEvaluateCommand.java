// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>flowId: 工作流id</li>
 *     <li>needEvaluate: 允许评价开关</li>
 *     <li>evaluateStart: 评价区间开始</li>
 *     <li>evaluateEnd: 评价区间结束</li>
 *     <li>evaluateStep: 评价完成后的动作</li>
 *     <li>messageAction: 消息信息{@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>smsAction: 短信信息{@link com.everhomes.rest.flow.FlowActionInfo}</li>
 *     <li>items: 评价项列表{@link com.everhomes.rest.flow.FlowEvaluateItemDTO}</li>
 * </ul>
 */
public class UpdateFlowEvaluateCommand {

	private Long flowId;
	private Byte needEvaluate;
	private Long evaluateStart;
	private Long evaluateEnd;
	private String evaluateStep;
	
	private FlowActionInfo messageAction;
	private FlowActionInfo smsAction;
	
	@ItemType(FlowEvaluateItemDTO.class)
	private List<FlowEvaluateItemDTO> items;

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

    public List<FlowEvaluateItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FlowEvaluateItemDTO> items) {
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
