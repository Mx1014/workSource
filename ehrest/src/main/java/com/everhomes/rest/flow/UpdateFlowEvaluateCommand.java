// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowId: 工作流id</li>
 *     <li>allowFlowCaseEndEvaluate: 允许结束后评价开关</li>
 * </ul>
 */
public class UpdateFlowEvaluateCommand {

    private Long flowId;

    private Byte allowFlowCaseEndEvaluate;

    // @ItemType(FlowEvaluateItemDTO.class)
    // private List<FlowEvaluateItemDTO> items;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
