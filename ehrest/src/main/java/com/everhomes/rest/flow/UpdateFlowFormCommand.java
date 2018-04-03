package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>formOriginId: formOriginId</li>
 *     <li>formVersion: formVersion</li>
 * </ul>
 */
public class UpdateFlowFormCommand {

    private Long flowId;
    private Long formOriginId;
    private Long formVersion;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
