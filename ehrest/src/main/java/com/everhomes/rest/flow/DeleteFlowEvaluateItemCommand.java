// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowId: 工作流id</li>
 *     <li>itemId: itemId</li>
 * </ul>
 */
public class DeleteFlowEvaluateItemCommand {

    private Long flowId;
    private Long itemId;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
