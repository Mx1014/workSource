// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>validationStatus: 校验状态 {@link com.everhomes.rest.flow.FlowValidationStatus}</li>
 * </ul>
 */
public class UpdateFlowValidationStatusCommand {

    @NotNull
    private Long flowId;
    @NotNull
    private Byte validationStatus;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Byte getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(Byte validationStatus) {
        this.validationStatus = validationStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
