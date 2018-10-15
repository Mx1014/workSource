package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowIdVersionCommand {

    private Long flowId;

    private Integer flowVersion ;

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
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
