// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class GetFlowGraphCommand {

    private Long flowId;

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
