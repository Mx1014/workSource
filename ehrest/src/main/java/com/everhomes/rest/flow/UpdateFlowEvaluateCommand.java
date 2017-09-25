// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>flowId: 工作流id</li>
 *     <li>allowFlowCaseEndEvaluate: 允许结束后评价开关</li>
 *     <li>items: 评价项列表{@link com.everhomes.rest.flow.FlowEvaluateItemDTO}</li>
 * </ul>
 */
public class UpdateFlowEvaluateCommand {

    private Long flowId;
    // private Byte needEvaluate;
    // private Long evaluateStart;
    // private Long evaluateEnd;
    // private String evaluateStep;

    private Byte allowFlowCaseEndEvaluate;

    // private FlowActionInfo messageAction;
    // private FlowActionInfo smsAction;

    @ItemType(FlowEvaluateItemDTO.class)
    private List<FlowEvaluateItemDTO> items;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Byte getAllowFlowCaseEndEvaluate() {
        return allowFlowCaseEndEvaluate;
    }

    public void setAllowFlowCaseEndEvaluate(Byte allowFlowCaseEndEvaluate) {
        this.allowFlowCaseEndEvaluate = allowFlowCaseEndEvaluate;
    }

    public List<FlowEvaluateItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FlowEvaluateItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
