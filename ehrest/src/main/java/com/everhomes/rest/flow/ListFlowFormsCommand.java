package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 * </ul>
 */
public class ListFlowFormsCommand {

    @NotNull private Long flowId;

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
