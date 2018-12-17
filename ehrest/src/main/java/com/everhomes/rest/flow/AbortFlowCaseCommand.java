package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>currentNodeId: currentNodeId</li>
 *     <li>stepCount: stepCount</li>
 * </ul>
 */
public class AbortFlowCaseCommand {

    @NotNull
    private Long flowCaseId;
    @NotNull
    private Long currentNodeId;
    @NotNull
    private Long stepCount;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Long getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Long currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public Long getStepCount() {
        return stepCount;
    }

    public void setStepCount(Long stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
